package com.gandalp.gandalp.schedule.domain.dto;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.schedule.domain.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ScheduleResponseDto {

    private Long offScheduleId;

    private Long nurseId;

    private String codeLabel;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime updatedAt;
}
