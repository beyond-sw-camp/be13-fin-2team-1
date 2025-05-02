package com.gandalp.gandalp.member.domain.repository;

import com.gandalp.gandalp.member.domain.dto.NurseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NurseRepositoryCustom {

   Page<NurseResponseDto> getAll(String keyword, Pageable pageable, Long departmentId);
}
