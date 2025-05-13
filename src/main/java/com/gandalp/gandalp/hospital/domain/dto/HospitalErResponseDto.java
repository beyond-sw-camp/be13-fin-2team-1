package com.gandalp.gandalp.hospital.domain.dto;

import com.gandalp.gandalp.hospital.domain.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalErResponseDto {

    private String name;
    private int totalErCount;
    private int availableErCount;

    public HospitalErResponseDto(Hospital hospital){
        this.name = hospital.getName();
        this.totalErCount = hospital.getTotalErCount();
        this.availableErCount = hospital.getAvailableErCount();
    }

}
