package com.gandalp.gandalp.schedule.domain.repository;

import com.gandalp.gandalp.schedule.domain.entity.ScheduleTemp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleTempRepository extends JpaRepository<ScheduleTemp, Long> {

    List<ScheduleTemp> findAllByNurseEmail(String email);
}
