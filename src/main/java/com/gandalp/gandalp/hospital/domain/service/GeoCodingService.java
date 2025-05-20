package com.gandalp.gandalp.hospital.domain.service;

import com.gandalp.gandalp.hospital.domain.dto.GeoResponse;
import com.gandalp.gandalp.hospital.domain.entity.Hospital;
import com.gandalp.gandalp.hospital.domain.repository.HospitalGeoRedisRepository;
import com.gandalp.gandalp.hospital.domain.repository.HospitalRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeoCodingService {

    private final HospitalRepository hospitalRepository;
    private final HospitalGeoRedisRepository hospitalGeoRedisRepository;
    private final NaverGeoClient naverGeoClient;


    //DB에 저장된 병원 주소를 위도/경도로 변환 ( 앱 실행, 병원 정보가 수정되는 경우에만 실행)
    @Transactional
    public void convertAllHospitalAddressToGeo(){

        List<Hospital> hospitals =
                hospitalRepository.findByLatitudeIsNullOrLongitudeIsNull();


        for(Hospital hospital: hospitals){

            // 여기서 오류
            GeoResponse geo = naverGeoClient.getGeoPointFromAddress(hospital.getAddress());

            if (geo != null) {

                hospital.updateGeoCode(geo.getLatitude(), geo.getLongitude());


                // Redis 저장을 hospitalGeoRedisRepository에서 하게 함
                hospitalGeoRedisRepository.saveHospitalLocation(
                        hospital.getId(),
                        geo.getLongitude(),
                        geo.getLatitude()
                );

            }
        }

        System.out.println(
                "GeoCodingService: 신규 병원 " + hospitals.size() +" 건 좌표 DB/Redis 저장 완료");
    }
}
