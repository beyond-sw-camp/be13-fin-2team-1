package com.gandalp.gandalp.member.domain.dto;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class NurseResponseDto {

    private final Long nurseId;

    private final String name;

    private final String email;

    public NurseResponseDto(Nurse nurse) {
        this.nurseId = nurse.getId();
        this.name = nurse.getName();
        this.email = nurse.getEmail();
    }
}
