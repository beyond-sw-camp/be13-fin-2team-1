package com.gandalp.gandalp.member.domain.repository;

import com.gandalp.gandalp.member.domain.entity.SurgeryNurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurgeryNurseRepository extends JpaRepository<SurgeryNurse, Long> {

    @Query("SELECT sn FROM SurgeryNurse sn WHERE sn.surgerySchedule.id = :surgeryScheduleId")
    List<SurgeryNurse> findBySurgeryScheduleId(@Param("surgeryScheduleId") Long surgeryScheduleId);

}
