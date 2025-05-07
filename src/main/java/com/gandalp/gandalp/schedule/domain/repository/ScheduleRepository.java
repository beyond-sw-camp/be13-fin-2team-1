package com.gandalp.gandalp.schedule.domain.repository;

import com.gandalp.gandalp.schedule.domain.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.nurse.email = :email " +
            "AND s.endTime > :startTime " +
            "AND s.startTime < :endTime")
    List<Schedule> findOverlappingSchedules(@Param("email") String email,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
}
