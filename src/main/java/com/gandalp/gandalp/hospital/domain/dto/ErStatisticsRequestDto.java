package com.gandalp.gandalp.hospital.domain.dto;

import com.gandalp.gandalp.hospital.domain.entity.ErOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErStatisticsRequestDto {

    private ErOption erOption;
    private Integer year;
    private Integer month;
    private Integer day;
}
