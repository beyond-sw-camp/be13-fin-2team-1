package com.gandalp.gandalp.hospital.domain.repository;

import com.gandalp.gandalp.hospital.domain.dto.ErStatisticsRequestDto;
import com.gandalp.gandalp.hospital.domain.dto.ErStatisticsResponseDto;
import com.gandalp.gandalp.hospital.domain.entity.Hospital;

import java.util.List;

public interface ErStatisticsRepositoryCustom {
    List<ErStatisticsResponseDto> findByErStatistics(ErStatisticsRequestDto requestDto, Hospital hospital);
}
