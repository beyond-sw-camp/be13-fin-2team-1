package com.gandalp.gandalp.schedule.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gandalp.gandalp.schedule.domain.dto.SurgeryScheduleResponseDto;
import com.gandalp.gandalp.schedule.domain.entity.SurgerySchedule;

public interface SurgeryScheduleRepositoryCustom {

	boolean isNurseInSurgery(Long nurseId, LocalDateTime now);
	List<SurgeryScheduleResponseDto> getAllSurgerySchedule(Long departmentId);

	int countByNurseAndMonth(Long nurseId, LocalDateTime start, LocalDateTime end);
}
