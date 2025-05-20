package com.gandalp.gandalp.calender.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalScheduleUpdateRequestDto {

    private Long scheduleId;

    private String password;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
