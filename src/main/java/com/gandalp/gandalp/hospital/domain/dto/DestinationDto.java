package com.gandalp.gandalp.hospital.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationDto {


    /**
     * 병원 고유 ID (Redis key 로도 사용)
     */
    private Long hospitalId;


    private double latitude;


    private double longitude;

}
