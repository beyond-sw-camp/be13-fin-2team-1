package com.gandalp.gandalp.schedule;

import com.gandalp.gandalp.member.domain.dto.NurseResponseDto;
import com.gandalp.gandalp.member.domain.repository.NurseRepository;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleRequestDto;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleTempResponseDto;
import com.gandalp.gandalp.schedule.domain.entity.Schedule;
import com.gandalp.gandalp.schedule.domain.entity.ScheduleTemp;
import com.gandalp.gandalp.schedule.domain.service.ScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/off")
    public ResponseEntity<OffScheduleTempResponseDto> createOffSchedule(@RequestBody OffScheduleRequestDto scheduleRequestDto) {
        OffScheduleTempResponseDto offScheduleTempResponseDto = null;
        try {
            offScheduleTempResponseDto = scheduleService.createOffSchecule(scheduleRequestDto);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return offScheduleTempResponseDto == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(offScheduleTempResponseDto);
    }

    @DeleteMapping("/off")
    public ResponseEntity<Void> deleteOffSchedule(@RequestBody Long scheduleTempId) {
        try {
            scheduleService.deleteOffScheduleTemp(scheduleTempId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/off")
    public ResponseEntity<List<OffScheduleTempResponseDto>> getOffSchedule(String email) {
        List<OffScheduleTempResponseDto> offScheduleTempResponseDtos = null;
        try {
            offScheduleTempResponseDtos = scheduleService.getOffScheduleTemp(email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(offScheduleTempResponseDtos);
    }

    // 이메일과 비밀번호 체크
    @PostMapping("/check")
    public ResponseEntity<NurseResponseDto> checkPassword(String password, String email) {
        NurseResponseDto nurseResponseDto = null;
        try {
            nurseResponseDto = scheduleService.checkPassword(password, email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(nurseResponseDto);
    }


}
