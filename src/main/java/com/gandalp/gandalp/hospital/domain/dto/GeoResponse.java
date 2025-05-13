package com.gandalp.gandalp.hospital.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeoResponse {
    // 병원 주소를 위도/ 경도로 변환
    private double latitude;
    private double longitude;
}
