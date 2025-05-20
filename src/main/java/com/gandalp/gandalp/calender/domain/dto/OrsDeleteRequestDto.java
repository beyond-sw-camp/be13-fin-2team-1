package com.gandalp.gandalp.calender.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrsDeleteRequestDto {

    private Long surgeryScheduleId;

    private String password;
}
