package com.gandalp.gandalp.schedule.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.gandalp.gandalp.schedule.domain.entity.Category;
import com.gandalp.gandalp.schedule.domain.entity.ScheduleTemp;
import com.gandalp.gandalp.schedule.domain.entity.TempCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.schedule.domain.entity.Schedule;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.nurse.id = :nurseId " +
            "AND s.endTime > :startTime " +
            "AND s.startTime < :endTime")
    List<Schedule> findOverlappingSchedules(@Param("nurseId") Long nurseId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);


    // 간호사 전체 일정 조회 (시작 ~ 끝)
    // 시작 시간이 범위에 들어오는 일정만 조회 ( day, evening, night 구분을 위해 만들었음)
    List<Schedule> findByNurseAndStartTimeBetween(Nurse nurse, LocalDateTime start, LocalDateTime end);


    List<Schedule> findAllByNurse(Nurse nurse);

    Optional<Schedule> findByNurseAndStartTimeAndCategory (Nurse nurse, LocalDateTime startTime, Category category);

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.nurse.id = :nurseId")
    List<Schedule> findByNurseId(@Param("nurseId") Long nurseId);

}
