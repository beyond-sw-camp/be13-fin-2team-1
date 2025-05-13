package com.gandalp.gandalp.hospital.domain.service;

import com.gandalp.gandalp.hospital.domain.dto.GeoResponse;
import com.gandalp.gandalp.hospital.domain.entity.Hospital;
import com.gandalp.gandalp.hospital.domain.repository.HospitalGeoRedisRepository;
import com.gandalp.gandalp.hospital.domain.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeoCodingService {

    private final HospitalRepository hospitalRepository;
    private final HospitalGeoRedisRepository hospitalGeoRedisRepository;
    private final NaverMapClient naverMapClient;

    //DB에 저장된 병원 주소를 위도/경도로 변환
    public void convertAllHospitalAddressToGeo(){

        List<Hospital> hospitals = hospitalRepository.findAll();

        for(Hospital hospital: hospitals){
            GeoResponse geo = naverMapClient.getGeoPointFromAddress(hospital.getAddress());

            if (geo != null) {
                // Redis 저장을 hospitalGeoRedisRepository로 위임
                hospitalGeoRedisRepository.saveHospitalLocation(
                        hospital.getId(),
                        geo.getLongitude(),
                        geo.getLatitude()
                );

            }
        }
    }
}
