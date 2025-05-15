package com.gandalp.gandalp.member.domain.repository;

import com.gandalp.gandalp.member.domain.dto.NurseResponseDto;
import com.gandalp.gandalp.member.domain.entity.NurseSearchOption;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NurseRepositoryCustom {

   Page<NurseResponseDto> getAll(String keyword, NurseSearchOption searchOption, Pageable pageable, Long departmentId);
}
