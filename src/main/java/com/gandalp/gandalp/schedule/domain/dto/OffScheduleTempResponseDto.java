package com.gandalp.gandalp.schedule.domain.dto;

import com.gandalp.gandalp.schedule.domain.entity.Category;
import com.gandalp.gandalp.schedule.domain.entity.TempCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class OffScheduleTempResponseDto {

    private final Long offScheduleTempId;

    private final String nurseName;

    private final TempCategory category;

    private final String content;

    private final LocalDateTime startTime;

    private final LocalDateTime endTime;
}
