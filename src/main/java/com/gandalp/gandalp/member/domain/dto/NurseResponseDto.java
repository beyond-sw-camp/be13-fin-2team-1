package com.gandalp.gandalp.member.domain.dto;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NurseResponseDto {


    private String name;

    private String email;


    public NurseResponseDto(Nurse nurse){
        this.name = nurse.getName();
        this.email = nurse.getEmail();
    }


}
