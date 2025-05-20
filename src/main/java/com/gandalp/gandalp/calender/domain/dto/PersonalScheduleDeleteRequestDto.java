package com.gandalp.gandalp.calender.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalScheduleDeleteRequestDto {

    private Long scheduleId;

    private String password;
}
