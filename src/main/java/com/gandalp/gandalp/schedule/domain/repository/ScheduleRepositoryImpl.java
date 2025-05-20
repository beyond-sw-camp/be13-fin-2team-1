package com.gandalp.gandalp.schedule.domain.repository;

import static com.gandalp.gandalp.schedule.domain.entity.QSchedule.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.member.domain.entity.NurseStatistics;
import com.gandalp.gandalp.member.domain.entity.Status;
import com.gandalp.gandalp.member.domain.repository.NurseStatisticsRepository;
import com.gandalp.gandalp.schedule.domain.dto.StaticsResponseDto;
import com.gandalp.gandalp.schedule.domain.entity.Category;
import com.gandalp.gandalp.schedule.domain.entity.Schedule;
import com.gandalp.gandalp.schedule.domain.entity.SelectOption;
import com.gandalp.gandalp.schedule.domain.entity.Work;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final NurseStatisticsRepository nurseStatisticsRepository;

	// 근무 시간
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



	@Override
	public StaticsResponseDto getNursesWorkingStatistics(Nurse nurse, SelectOption selectOption, int year, int month, Integer quarter) {


		StaticsResponseDto.StaticsResponseDtoBuilder builder = StaticsResponseDto.builder()
				.nurseId(nurse.getId())
				.nurseName(nurse.getName());

		if (selectOption == null || selectOption == SelectOption.MONTH  ) {
			// 전 달

			nurseStatisticsRepository.findByNurseIdAndYearAndMonth(nurse.getId(), year, month).ifPresent(stats -> nurseStats(builder, stats));

			return builder.build();

		}

		if ( selectOption == SelectOption.YEAR ){

			List<NurseStatistics> nurseStatistics = nurseStatisticsRepository.findByNurseIdAndYear(nurse.getId(), year);
			if (nurseStatistics.isEmpty()){

				return builder.build();
			}

			return sum(nurse, nurseStatistics);

		}

		if(selectOption == SelectOption.QUARTER) {


			if (quarter == null || quarter < 1 || quarter > 4) {
				throw new IllegalArgumentException("1~4 분기 중 하나를 선택해주세요.");
			}
			
			int startMonth = (quarter-1) * 3 + 1;
			int endMonth = startMonth + 2;

			List<NurseStatistics> nurseStatistics = nurseStatisticsRepository.findByNurseAndYearAndMonthBetween(nurse, year, startMonth, endMonth);
			if(nurseStatistics.isEmpty()){

				return builder.build();
			}

			return sum(nurse, nurseStatistics);
		}


		return builder.build();
    }

	private void nurseStats(
			StaticsResponseDto.StaticsResponseDtoBuilder b,
			NurseStatistics s) {

		b.dayCount(s.getDayCount())
				.eveningCount(s.getEveningCount())
				.nightCount(s.getNightCount())
				.offCount(s.getOffCount())
				.surgeryCount(s.getSurgeryCount());
	}


	private StaticsResponseDto sum(Nurse nurse, List<NurseStatistics> nurseStatistics){

		int day = 0, night = 0, evening = 0, off = 0, surgery = 0;

		for (NurseStatistics ns : nurseStatistics) {
			day += ns.getDayCount();
			night += ns.getNightCount();
			evening += ns.getEveningCount();
			off += ns.getOffCount();
			surgery += ns.getSurgeryCount();
		}

		return StaticsResponseDto.builder()
			.nurseId(nurse.getId())
			.nurseName(nurse.getName())
			.dayCount(day)
			.eveningCount(evening)
			.nightCount(night)
			.offCount(off)
			.surgeryCount(surgery)
			.build();

	}

}
