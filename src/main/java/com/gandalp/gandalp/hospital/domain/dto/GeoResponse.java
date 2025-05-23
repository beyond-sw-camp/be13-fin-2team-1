package com.gandalp.gandalp.hospital.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class GeoResponse implements Serializable {
    // 병원 주소를 위도/ 경도로 변환
    private double longitude; // 경도
    private double latitude; // 위도
}
