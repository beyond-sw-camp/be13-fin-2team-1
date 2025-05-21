package com.gandalp.gandalp.hospital.domain.service;

import com.gandalp.gandalp.auth.model.service.AuthService;
import com.gandalp.gandalp.hospital.domain.dto.ErStatisticsRequestDto;
import com.gandalp.gandalp.hospital.domain.dto.ErStatisticsResponseDto;
import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.hospital.domain.entity.ErStatistics;
import com.gandalp.gandalp.hospital.domain.entity.Hospital;
import com.gandalp.gandalp.hospital.domain.repository.ErStatisticsRepository;
import com.gandalp.gandalp.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ErStatisticsService {

    private final AuthService authService;
    private final ErStatisticsRepository erStatisticsRepository;


    public List<ErStatisticsResponseDto> getStatistics(ErStatisticsRequestDto requestDto){

        // 1. 로그인 했는지 검증
        Member member = authService.getLoginMember();

        // 2. 로그인 한 사람의 병원 가져오기
        Hospital hospital = member.getHospital();

        // 3. list 받아오기
        List<ErStatisticsResponseDto> result = erStatisticsRepository.findByErStatistics(requestDto, hospital);

        return result;
    }
}

