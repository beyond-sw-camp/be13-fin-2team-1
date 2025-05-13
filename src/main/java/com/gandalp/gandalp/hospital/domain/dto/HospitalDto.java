package com.gandalp.gandalp.hospital.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HospitalDto {

    private String name;
    private String address;
    private String phoneNumber;
    private int totalErCount;
    private int availableErCount;

}

