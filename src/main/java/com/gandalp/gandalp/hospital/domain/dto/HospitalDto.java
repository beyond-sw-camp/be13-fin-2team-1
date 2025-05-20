package com.gandalp.gandalp.hospital.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDto {

    private Long   id;
    private String name;
    private String address;
    private String phoneNumber;
    private int totalErCount;
    private int availableErCount;
    private double latitude;
    private double longitude;

}

