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

    private Long id;

    private String name;

    private String email;


    public NurseResponseDto(Nurse nurse){
        this.id = nurse.getId();
        this.name = nurse.getName();
        this.email = nurse.getEmail();
    }


}
