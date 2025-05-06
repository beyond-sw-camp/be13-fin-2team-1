package com.gandalp.gandalp.schedule.domain.dto;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.schedule.domain.entity.Category;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OffScheduleRequestDto {

    private final String email;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
