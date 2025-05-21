package com.gandalp.gandalp.hospital.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErStatisticsResponseDto {

    private Long hospitalId;

    private int year;

    private int month;

    private int day;

    private int hour;

    private int count;
}
