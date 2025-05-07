package com.gandalp.gandalp.member.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.hospital.domain.repository.DepartmentRepository;
import com.gandalp.gandalp.member.domain.dto.NurseCurrentStatusDto;
import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.member.domain.entity.Status;
import com.gandalp.gandalp.member.domain.repository.NurseRepository;
import com.gandalp.gandalp.schedule.domain.repository.ScheduleRepository;
import com.gandalp.gandalp.schedule.domain.repository.SurgeryScheduleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NurseService {

	private final NurseRepository nurseRepository;
	private final DepartmentRepository departmentRepository;
	private final ScheduleRepository scheduleRepository;
	private final SurgeryScheduleRepository surgeryScheduleRepository;

	///  간호사들의 현재 상태 조회
	public List<NurseCurrentStatusDto> getNurseStatus(Department dept){

		Department department = departmentRepository.findById(dept.getId()).orElseThrow(
			() -> new EntityNotFoundException("해당하는 과가 존재하지 않습니다.")
		);

		List<Nurse> nurses = nurseRepository.findByDepartment(department);


		return nurses.stream()
			.map(nurse -> new NurseCurrentStatusDto(nurse.getName(), nurse.getWorkingStatus() ))
			.toList();
	}

	// 간호사들 현재 상태 수정
	@Transactional
	public NurseCurrentStatusDto updateNurseStatus( String email, Status workingStatus){


		// 이메일로 해당 nurse가 존재하는지 검증

		Nurse nurse = nurseRepository.findByEmail(email).orElseThrow(

			() -> new EntityNotFoundException("해당 간호사가 존재하지 않습니다.")
		);

		// 근무 상태 수정
		if(workingStatus == Status.ON)
			nurse.updateWorkingStatus(Status.ON);
		else if(workingStatus == Status.OFF)
			nurse.updateWorkingStatus(Status.OFF);
		else if(workingStatus == Status.IN_SURGERY)
			nurse.updateWorkingStatus(Status.IN_SURGERY);

		return new NurseCurrentStatusDto(nurse);
	}


	///  간호사들 근무 상태 자동 변환
	@Scheduled(cron = "0 0 * * * *") // 1시간마다 갱신
	@Transactional
	public void autoUpdateNurseStatus(){
		// 과, 병원 필요없고 DB 에 존재하는 모든 간호사들의 일정 확인 후 상태 전환


		LocalDateTime now = LocalDateTime.now();
		// 1. 모든 간호사 조회
		// 2. 현재시간을 기준으로 수술중인지 체크
		// 3. 현재시간을 기준으로 근무중인지 체크

		List<Nurse> allNurse = nurseRepository.findAll();
		for(Nurse nurse: allNurse){

			/// for log - 삭제하기
			Status before = nurse.getWorkingStatus();


			// 1. 수술중인지
			if (surgeryScheduleRepository.isNurseInSurgery(nurse.getId(), now)){
				nurse.updateWorkingStatus(Status.IN_SURGERY);
				continue;
			}


			// 2. 일반 진료근무중인지 확인
			if (scheduleRepository.findCurrentSchedule(nurse.getId(), now)){
				nurse.updateWorkingStatus(Status.ON);
				continue;
			}

			// 3. 둘다 아니면 off !
			nurse.updateWorkingStatus(Status.OFF);

			/// for log - 삭제하기
			Status after = nurse.getWorkingStatus();
			if (!before.equals(after)) {
				nurse.updateWorkingStatus(after);
				log.info("간호사 [{}] 상태 변경: {} → {}", nurse.getName(), before, after);
			}

		}
	}





















}

