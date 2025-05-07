package com.gandalp.gandalp.schedule.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gandalp.gandalp.schedule.domain.entity.SurgerySchedule;

@Repository
public interface SurgeryScheduleRepository extends JpaRepository<SurgerySchedule,Long>, SurgeryScheduleRepositoryCustom {
}
