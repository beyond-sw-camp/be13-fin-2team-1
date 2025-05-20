package com.gandalp.gandalp.calender.domain.dto;

import com.gandalp.gandalp.schedule.domain.entity.Category;
import com.gandalp.gandalp.schedule.domain.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalScheduleResponseDto {

    private Long scheduleId;

    private Long nurseId;

    private Category category;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public static PersonalScheduleResponseDto fromSchedule(Schedule schedule) {
        return PersonalScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .nurseId(schedule.getNurse().getId())
                .category(schedule.getCategory())
                .content(schedule.getContent())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }

}
