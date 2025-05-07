package com.gandalp.gandalp.schedule.domain.repository;

import java.time.LocalDateTime;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
}
