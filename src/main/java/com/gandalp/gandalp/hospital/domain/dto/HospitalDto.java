package com.gandalp.gandalp.hospital.domain.dto;

import com.gandalp.gandalp.hospital.domain.entity.Hospital;
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

    public HospitalDto(Hospital hospital) {
        this.id               = hospital.getId();
        this.name             = hospital.getName();
        this.address          = hospital.getAddress();
        this.phoneNumber      = hospital.getPhoneNumber();
        this.totalErCount     = hospital.getTotalErCount();
        this.availableErCount = hospital.getAvailableErCount();
        this.latitude         = hospital.getLatitude();
        this.longitude        = hospital.getLongitude();
    }
}

