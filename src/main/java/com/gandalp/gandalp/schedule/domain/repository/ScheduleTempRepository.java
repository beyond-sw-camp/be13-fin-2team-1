package com.gandalp.gandalp.schedule.domain.repository;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.schedule.domain.entity.ScheduleTemp;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleTempRepository extends JpaRepository<ScheduleTemp, Long> {

    List<ScheduleTemp> findAllByNurseEmail(String email);

    Optional<ScheduleTemp> findById(Long schduleTempId);

    @Query("SELECT s FROM ScheduleTemp s " +
            "WHERE s.nurse.id = :nurseId " +
            "AND s.endTime > :startTime " +
            "AND s.startTime < :endTime")
    List<ScheduleTemp> findOverlappingTempSchedules(@Param("nurseId") Long nurseId,
                                                    @Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime);
    List<ScheduleTemp> findByNurseIn(List<Nurse> nurseList);

    List<ScheduleTemp> findAllByNurse(Nurse nurse);
}
