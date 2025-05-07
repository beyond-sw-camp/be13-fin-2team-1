package com.gandalp.gandalp.schedule.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.gandalp.gandalp.schedule.domain.dto.SurgeryScheduleResponseDto;

public interface SurgeryScheduleRepositoryCustom {
	boolean isNurseInSurgery(Long nurseId, LocalDateTime now);

	List<SurgeryScheduleResponseDto> getAllSurgerySchedule(Long departmentId);
}
