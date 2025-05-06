package com.gandalp.gandalp.schedule.domain.repository;

import com.gandalp.gandalp.schedule.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
