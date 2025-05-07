package com.gandalp.gandalp.schedule.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.hospital.domain.repository.DepartmentRepository;
import com.gandalp.gandalp.schedule.domain.dto.SurgeryScheduleResponseDto;
import com.gandalp.gandalp.schedule.domain.entity.SurgerySchedule;
import com.gandalp.gandalp.schedule.domain.repository.SurgeryScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurgeryScheduleService {

	private final SurgeryScheduleRepository surgeryScheduleRepository;
	private final DepartmentRepository departmentRepository;


	public List<SurgeryScheduleResponseDto> getAllSurgerySchedule(Long departmentId){

		// 1. 부서 조회
		Department department = departmentRepository.findById(departmentId).orElseThrow(
			() -> new IllegalArgumentException("소속된 과가 없습니다.")
		);



		// 해당 과의 스케줄 전체 조회
		List<SurgeryScheduleResponseDto> surgerScheduleList = surgeryScheduleRepository.getAllSurgerySchedule(department.getId());

		return surgerScheduleList;
	}


}
