package com.gandalp.gandalp.member.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.member.domain.entity.NurseStatistics;

public interface NurseStatisticsRepository extends JpaRepository<NurseStatistics, Long> {

	Optional<NurseStatistics> findByNurseIdAndYearAndMonth(Long nurseId ,int year,int month);

	List<NurseStatistics> findByNurseIdAndYear(Long nurseId, int year);

	List<NurseStatistics> findByNurseAndYearAndMonthBetween(Nurse nurse, int year,int startMonth,int endMonth);
}
