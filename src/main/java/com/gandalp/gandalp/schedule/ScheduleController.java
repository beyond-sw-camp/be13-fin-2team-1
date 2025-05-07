package com.gandalp.gandalp.schedule;

import com.gandalp.gandalp.auth.model.dto.CustomUserDetails;
import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.member.domain.dto.NurseResponseDto;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.repository.NurseRepository;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleRequestDto;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleResponseDto;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleTempResponseDto;
import com.gandalp.gandalp.schedule.domain.dto.SurgeryScheduleResponseDto;
import com.gandalp.gandalp.schedule.domain.entity.Schedule;
import com.gandalp.gandalp.schedule.domain.entity.ScheduleTemp;
import com.gandalp.gandalp.schedule.domain.repository.SurgeryScheduleRepository;
import com.gandalp.gandalp.schedule.domain.service.ScheduleService;
import com.gandalp.gandalp.schedule.domain.service.SurgeryScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final SurgeryScheduleService surgeryScheduleService;

    @PostMapping("/off")
    public ResponseEntity<?> createOffSchedule(@RequestBody OffScheduleRequestDto scheduleRequestDto) {
        OffScheduleTempResponseDto offScheduleTempResponseDto = null;
        try {
            offScheduleTempResponseDto = scheduleService.createOffSchecule(scheduleRequestDto);
        }
        catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return offScheduleTempResponseDto == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(offScheduleTempResponseDto);
    }

    @DeleteMapping("/off")
    public ResponseEntity<?> deleteOffSchedule(@RequestBody Long scheduleTempId) {
        try {
            scheduleService.deleteOffScheduleTemp(scheduleTempId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/off")
    public ResponseEntity<?> getOffSchedule(String email) {
        List<OffScheduleTempResponseDto> offScheduleTempResponseDtos = null;
        try {
            offScheduleTempResponseDtos = scheduleService.getOffScheduleTemp(email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(offScheduleTempResponseDtos);
    }

    // 승인 대기 중인 오프 관리
    // 임시 스케줄에 있는 오프 승인 시 -> 처리됨으로 바뀌고 스케쥴에 카테고리 승인된 오프로 바뀌고 삽입
    @PostMapping("/acceptOff/{schedule-temp-id}")
    @PreAuthorize("hasRole('HEAD_NURSE')")
    public ResponseEntity<?> acceptOffSchedule(@PathVariable("schedule-temp-id") Long scheduleTempId) {
        try {
            OffScheduleResponseDto offScheduleResponseDto = scheduleService.acceptOff(scheduleTempId);
            return ResponseEntity.ok().body(offScheduleResponseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 임시 스케줄에 있는 오프 반려 시 -> 처리됨으로 만 바뀜

    // 이메일과 비밀번호 체크
    @PostMapping("/check")
    public ResponseEntity<?> checkPassword(String password, String email) {
        NurseResponseDto nurseResponseDto = null;
        try {
            nurseResponseDto = scheduleService.checkPassword(password, email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(nurseResponseDto);
    }


    // 수술 일정 조회
    @Operation(summary = "수술 일정 조회", description = "수간호사와 간호사가 수술 일정을 조회 가능")
    @GetMapping("/surgery")
    public ResponseEntity<List<SurgeryScheduleResponseDto>> getAllSurgerySchdule(Authentication auth){

        CustomUserDetails userDetails = (CustomUserDetails)auth.getPrincipal();
        Member member = userDetails.getMember();

        if (member == null){
            throw new EntityNotFoundException("로그인을 해주세요");
        }

        Department department = member.getDepartment();

        List<SurgeryScheduleResponseDto> surgerySchedules = surgeryScheduleService.getAllSurgerySchedule(department.getId());


        return ResponseEntity.ok(surgerySchedules);
    }





}
