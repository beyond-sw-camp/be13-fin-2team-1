package com.gandalp.gandalp.hospital.domain.service;

import com.gandalp.gandalp.hospital.domain.dto.DestinationDto;
import com.gandalp.gandalp.hospital.domain.dto.ErCountUpdateDto;
import com.gandalp.gandalp.hospital.domain.dto.GeoResponse;
import com.gandalp.gandalp.hospital.domain.dto.HospitalDto;
import com.gandalp.gandalp.hospital.domain.dto.HospitalErResponseDto;
import com.gandalp.gandalp.hospital.domain.entity.Hospital;
import com.gandalp.gandalp.hospital.domain.entity.SortOption;
import com.gandalp.gandalp.hospital.domain.repository.HospitalGeoRedisRepository;
import com.gandalp.gandalp.hospital.domain.repository.HospitalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalGeoRedisRepository hospitalGeoRedisRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final NaverMapClient naverMapClient;
    private final NaverDirectionClient naverDirectionClient;


    private static final String GEO_KEY = "hospital:geo";

    @Transactional
    public HospitalErResponseDto updateErCount(ErCountUpdateDto updateDto) {

        Hospital hospital = hospitalRepository.findById(updateDto.getHospitalId()).orElseThrow(
                () -> new EntityNotFoundException("해당 병원은 존재하지 않습니다.")
        );

        hospital.updateAvailableErCount(updateDto.getAvailableErCount());

        return new HospitalErResponseDto(hospital);
    }


    // 병원 좌표 갱신 ( 최초 1회 또는 주소 수정 시 )
    public void updateGeoPointFroAllHospitals() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        for (Hospital hospital : hospitals) {
            GeoResponse geo = naverMapClient.getGeoPointFromAddress((hospital.getAddress()));

            hospitalGeoRedisRepository.saveHospitalLocation(
                    hospital.getId(),
                    geo.getLongitude(),
                    geo.getLatitude()
            );
        }
    }

    // 검색이 없으면 기본 현재 위치에서 가까운 순으로 응급실 20곳 조회
    // 거리순, 가용 병상 순
    // 검색을 하는 경우 주소, 병원 이름
    public Page<HospitalDto> getNearestHospitals(double lat, double lon, String keyword, SortOption sortOption, Pageable pageable) {


        // 1) Redis Geo에서 반경 제한 없이 최단거리 후보 ID(최대 50개 정도 여유 있게) 조회
        List<Long> candidateIds = hospitalGeoRedisRepository.findNearbyHospitalIds(lat, lon, 50);

        // 만약 후보가 없다면 빈 페이지 반환
        if (candidateIds.isEmpty()) {
            throw new EntityNotFoundException("주변 병원이 없습니다.");
        }


        // 위에서 후보로 조회한 각 병원 ID에 대해 Redis에 저장한 위·경도 정보를 한꺼번에 조회
        List<Point> points = hospitalGeoRedisRepository.findLocationsByIds(candidateIds);



        // 3) Point 리스트를 네이버 Direction API용 DTO로 변환
        //    (DestinationDto는 id, lat, lon 같이 담고 있어야 합니다)
        List<DestinationDto> destinations = IntStream.range(0, candidateIds.size())
                .mapToObj(i -> new DestinationDto(
                        candidateIds.get(i),
                        points.get(i).getY(),  // Point(x=lon, y=lat)
                        points.get(i).getX()
                ))
                .collect(Collectors.toList());
        // 반환 예시: { hospitalId1 → 1.2km, hospitalId2 → 0.9km, … }

        // 4) 네이버 Direction API 호출 (service=15, batch size 최대 25)
        Map<Long, Double> roadDistances = naverDirectionClient.getRoadDistances(
                lat, lon,
                destinations
        );

        // 5) 거리 순 정렬 후 상위 20개 ID 뽑기
        List<Long> top20Ids = roadDistances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(20)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 6) 최종적으로 거리/검색/정렬 조건을 적용해 JPA로 조회
        return hospitalRepository.searchNearbyHospitals(top20Ids, keyword, sortOption, pageable);
    }



}
