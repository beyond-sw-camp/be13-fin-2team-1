package com.gandalp.gandalp.hospital.domain.repository;

import org.springframework.data.geo.Point;

import java.util.List;

public interface HospitalGeoRedisRepository {

    void saveHospitalLocation(Long hospitalId, double lon, double lat);

    // redis에서 주변 응급실 20곳 조회
    List<Long>findNearbyHospitalIds(double lat, double lon, int count);


    List<Point>findLocationsByIds(List<Long> candidateIds);


}
