package com.gandalp.gandalp.schedule.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WorkTempRequestUpdateDto {
    private final Long workTempId;

    private final String content;

    private final LocalDateTime startTime;

    private final LocalDateTime endTime;
}
