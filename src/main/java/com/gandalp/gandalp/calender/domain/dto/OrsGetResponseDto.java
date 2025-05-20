package com.gandalp.gandalp.calender.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrsGetResponseDto {

    private Long surgeryScheduleId;

    private List<Long> nurseIds;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
