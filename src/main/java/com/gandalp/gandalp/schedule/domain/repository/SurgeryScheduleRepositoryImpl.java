package com.gandalp.gandalp.schedule.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.gandalp.gandalp.schedule.domain.dto.SurgeryScheduleResponseDto;
import com.gandalp.gandalp.schedule.domain.entity.SurgerySchedule;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.gandalp.gandalp.hospital.domain.entity.QRoom.*;
import static com.gandalp.gandalp.member.domain.entity.QNurse.*;
import static com.gandalp.gandalp.member.domain.entity.QSurgeryNurse.surgeryNurse;
import static com.gandalp.gandalp.schedule.domain.entity.QSurgerySchedule.*;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SurgeryScheduleRepositoryImpl implements SurgeryScheduleRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public boolean isNurseInSurgery(Long nurseId, LocalDateTime now) {

		// nurseId 의 일정 중 surgery 가져오기
		// now 시간이 시작  <= now <= 끝 인지 확인하기

		Long count = queryFactory.select(surgeryNurse.count())
			.from(surgeryNurse)
			.innerJoin(surgeryNurse.surgerySchedule, surgerySchedule)
			.where(
				surgeryNurse.nurse.id.eq(nurseId),
				surgerySchedule.startTime.loe(now), // <=
				surgerySchedule.endTime.goe(now)    // >=
			)
			.fetchOne();



		return count != null && count > 0;
	}

	@Override
	public List<SurgeryScheduleResponseDto> getAllSurgerySchedule(Long departmentId) {


		List<SurgeryScheduleResponseDto> scheduleList  = queryFactory
			.select(Projections.constructor(SurgeryScheduleResponseDto.class,
				surgerySchedule.id,
				surgerySchedule.room.id,
				surgerySchedule.content,
				surgerySchedule.startTime,
				surgerySchedule.endTime
			))
			.from(surgerySchedule)
			.join(surgeryNurse).on(surgeryNurse.surgerySchedule.eq(surgerySchedule))
			.join(surgeryNurse.nurse, nurse)
			.where(nurse.department.id.eq(departmentId))
			.distinct()
			.fetch();


		for (SurgeryScheduleResponseDto dto : scheduleList) {
			List<String> nurseNames = getNurseNamesByScheduleId(dto.getSurgeryScheduleId());
			dto.setNurseNames(nurseNames);
		}

		return scheduleList;
	}

	@Override
	public int countByNurseAndMonth(Long nurseId, LocalDateTime start, LocalDateTime end) {
		Long count = queryFactory.select(surgeryNurse.count())
			.from(surgeryNurse)
			.join(surgeryNurse.surgerySchedule, surgerySchedule)
			.where(
				surgeryNurse.nurse.id.eq(nurseId),
				surgerySchedule.startTime.between(start, end)
			)
			.fetchOne();


		return count == null ? 0 : count.intValue();
	}

	// 수술 일정 별 참여하는 간호사 이름 조회
	public List<String> getNurseNamesByScheduleId(Long scheduleId) {
		return queryFactory
			.select(nurse.name)
			.from(surgeryNurse)
			.join(surgeryNurse.nurse, nurse)
			.where(surgeryNurse.surgerySchedule.id.eq(scheduleId))
			.fetch();
	}

}
