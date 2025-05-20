package com.gandalp.gandalp.calender.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrsRequestDto {

    private Long roomId;

    private List<Long> nurseIds = new ArrayList<>();

    private String password;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
