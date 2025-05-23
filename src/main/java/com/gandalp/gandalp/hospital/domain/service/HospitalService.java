package com.gandalp.gandalp.hospital.domain.service;

import com.gandalp.gandalp.auth.model.service.AuthService;
import com.gandalp.gandalp.hospital.domain.dto.DestinationDto;
import com.gandalp.gandalp.hospital.domain.dto.ErCountUpdateDto;
import com.gandalp.gandalp.hospital.domain.dto.GeoResponse;
import com.gandalp.gandalp.hospital.domain.dto.HospitalDto;
import com.gandalp.gandalp.hospital.domain.dto.HospitalErResponseDto;
import com.gandalp.gandalp.hospital.domain.entity.ErStatistics;
import com.gandalp.gandalp.hospital.domain.entity.Hospital;
import com.gandalp.gandalp.hospital.domain.entity.SortOption;
import com.gandalp.gandalp.hospital.domain.repository.ErStatisticsRepository;
import com.gandalp.gandalp.hospital.domain.repository.HospitalGeoRedisRepository;
import com.gandalp.gandalp.hospital.domain.repository.HospitalRepository;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.entity.Nurse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final NaverGeoClient naverGeoClient;
    private final NaverDirectionClient naverDirectionClient;
    private final ErStatisticsRepository erStatisticsRepository;

    private static final String GEO_KEY = "hospital:geo";
    private final ErStatisticsService erStatisticsService;
    private final AuthService authService;



    // 병원 단 건 조회
    public HospitalDto getOneHospital( ) {

        // 1. 로그인 했는지 검증
        Member member = authService.getLoginMember();

        // 2. 로그인 한 사람의 병원 가져오기
        Long hospitalId = member.getHospital().getId();

        Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(
                () -> new EntityNotFoundException("해당하는 병원이 존재하지 않습니다.")
        );


        return new HospitalDto(hospital);
    }

    @Transactional
    public HospitalErResponseDto updateErCount(ErCountUpdateDto updateDto) {

        // 1. 로그인 했는지 검증
        Member member = authService.getLoginMember();

        // 2. 로그인 한 사람의 병원 가져오기
        Hospital hospital = member.getHospital();

//        Hospital hospital = hospitalRepository.findById(updateDto.getHospitalId()).orElseThrow(
//                () -> new EntityNotFoundException("해당 병원은 존재하지 않습니다.")
//        );
//


        int pastErCount = hospital.getAvailableErCount();
        int nowErCount = hospital.updateAvailableErCount(updateDto.getAvailableErCount());

        int diff = pastErCount - nowErCount;
        if ( diff > 0 ) {

            // 과거 ErCount 가 현재 ErCount 보다 크면, 응급실 입원 환자수 존재 -> ErStatistics 에 넣기

            LocalDateTime now = LocalDateTime.now();
            ErStatistics statistics = ErStatistics.builder()
                                            .hospital(hospital)
                                            .year(now.getYear())
                                            .month(now.getMonthValue())
                                            .day(now.getDayOfMonth())
                                            .hour(now.getHour())
                                            .patients(diff)
                                            .build();

            erStatisticsRepository.save(statistics);
        }





        return new HospitalErResponseDto(hospital);
    }


    // 병원 좌표 갱신 ( 최초 1회 또는 주소 수정 시 )
    // 병원 추가나 주소 수정 api가 있는 경우
    public void updateGeoPointFroAllHospitals() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        for (Hospital hospital : hospitals) {
            GeoResponse geo = naverGeoClient.getGeoPointFromAddress((hospital.getAddress()));

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
    public Page<HospitalDto> getNearestHospitals(double longitude, double latitude, String keyword, SortOption sortOption, Pageable pageable) {



            // 1) Redis Geo에서 반경 제한 없이 최단거리 후보 ID(최대 50개 정도 여유 있게) 조회
            List<Long> candidateIds = hospitalGeoRedisRepository.findNearbyHospitalIds(latitude, longitude, 50);

            // 만약 후보가 없다면 빈 페이지 반환
            if (candidateIds.isEmpty()) {
                throw new EntityNotFoundException("주변 병원이 없습니다.");
            }


            // 위에서 후보로 조회한 각 병원 ID에 대해 Redis에 저장한 위·경도 정보를 한꺼번에 조회
            List<Point> points = hospitalGeoRedisRepository.findLocationsByIds(candidateIds);


            // 3) Point 리스트를 네이버 Direction API용 DTO로 변환
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
                    latitude, longitude,
                    destinations
            );

            // 5) 거리 순 정렬 후 상위 20개 ID 뽑기
            List<Long> top20Ids = roadDistances.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .limit(20)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // 6) 최종적으로 거리/검색/정렬 조건을 적용해 JPA로 조회

            Page<HospitalDto> page = hospitalRepository.searchNearbyHospitals(top20Ids, keyword, sortOption, pageable);



        return page;
    }



}
