package com.gandalp.gandalp.schedule;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gandalp.gandalp.member.domain.dto.NurseResponseDto;
import com.gandalp.gandalp.member.domain.entity.Status;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleRequestDto;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleResponseDto;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleTempResponseDto;
import com.gandalp.gandalp.schedule.domain.dto.StaticRequestDto;
import com.gandalp.gandalp.schedule.domain.dto.StaticsResponseDto;
import com.gandalp.gandalp.schedule.domain.dto.SurgeryScheduleResponseDto;
import com.gandalp.gandalp.schedule.domain.entity.SelectOption;
import com.gandalp.gandalp.schedule.domain.service.NurseStaticsService;
import com.gandalp.gandalp.schedule.domain.service.ScheduleService;
import com.gandalp.gandalp.schedule.domain.service.SurgeryScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final SurgeryScheduleService surgeryScheduleService;
    private final NurseStaticsService nurseStaticsService;

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
    public ResponseEntity<?> getAllSurgerySchedule(){

        List<SurgeryScheduleResponseDto> surgerySchedules = null;

        try{
             surgerySchedules = surgeryScheduleService.getAllSurgerySchedule();


        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


        return ResponseEntity.ok(surgerySchedules);
    }


    // 간호사 근무 분석
    // 오프 미 포함 근무, 수술 통계 조회
    // 아무것도 선택하지 않으면 이전 달의 근무 분석표가 보임
    // 옵션, 근무 상태( 기본 전체 다 보여줌 ) - 간호사별 업무 비율에서는 다 보여주고 간호사별 근무량 비교에서는 status를 선택으로 받아서
    @Operation(
        summary = "간호사 통합 통계 조회",
        description = "과에 소속된 간호사들의 근무/오프/수술 통계를 통합 조회합니다. status 값으로 필터링할 수 있습니다."
    )
    @PostMapping("/status/working")
    public ResponseEntity<?> getAllNurseWorking(@Valid @RequestBody StaticRequestDto staticRequestDto){

        List<StaticsResponseDto> workingStatics = null;

        try{
            workingStatics = nurseStaticsService.getWorkingStatics(staticRequestDto);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(workingStatics);
    }


    // 간호사 업무 비율 분석
    // 오프 포함 ( 수술, 오프, 근무(타입))


    // 응급실 통계


    // 누적 수술 수 조회
    // 해당 과의 간호사들의 수술 수를 월 별로 가져옴 + 전체 수술 수를 map으로 가져옴
    // @Operation(summary = "간호사 누적 수술 수 조회", description = "과에 소속된 모든 간호사들의 누적 수술수를 조회할 수 있다.")
    // @GetMapping("/status/operation")
    // public ResponseEntity<?> get
    //
    //
    //
    //




}
