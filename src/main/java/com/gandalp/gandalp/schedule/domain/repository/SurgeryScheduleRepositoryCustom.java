package com.gandalp.gandalp.schedule.domain.repository;

import java.time.LocalDateTime;

public interface SurgeryScheduleRepositoryCustom {
	boolean isNurseInSurgery(Long nurseId, LocalDateTime now);
}
