package com.gandalp.gandalp.schedule.domain.repository;

import static com.gandalp.gandalp.schedule.domain.entity.QSchedule.*;

import java.time.LocalDateTime;

import com.gandalp.gandalp.schedule.domain.entity.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom{

	private final JPAQueryFactory queryFactory;


	@Override
	public boolean findCurrentSchedule(Long nurseId, LocalDateTime now) {

		Long count = queryFactory.select(schedule.count())
			.from(schedule)
			.where(
				schedule.nurse.id.eq(nurseId),
				schedule.startTime.loe(now),
				schedule.endTime.goe(now),
				schedule.category.in(Category.WORKING, Category.PERSONAL)
			)
			.fetchOne();

		return count != null && count > 0;
	}
}
