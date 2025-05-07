package com.gandalp.gandalp.schedule.domain.repository;

import java.time.LocalDateTime;

public interface ScheduleRepositoryCustom {

	boolean findCurrentSchedule(Long nurseId, LocalDateTime now );
}
