package com.gandalp.gandalp.hospital.domain.service;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalGeoRedisRepository hospitalGeoRedisRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final NaverMapClient naverMapClient;


    private static final String GEO_KEY = "hospital:geo";

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
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        return hospitalRepository.searchNearbyHospitals(nearbyHospitalIds, keyword, sortOption, pageable);
    }



}
