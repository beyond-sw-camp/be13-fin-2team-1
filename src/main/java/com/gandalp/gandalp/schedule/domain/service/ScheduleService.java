package com.gandalp.gandalp.schedule.domain.service;

import com.gandalp.gandalp.common.repository.CommonCodeRepository;
import com.gandalp.gandalp.member.domain.dto.NurseResponseDto;
import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.member.domain.repository.NurseRepository;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleRequestDto;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleResponseDto;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleTempResponseDto;
import com.gandalp.gandalp.schedule.domain.entity.Category;
import com.gandalp.gandalp.schedule.domain.entity.Schedule;
import com.gandalp.gandalp.schedule.domain.entity.ScheduleTemp;
import com.gandalp.gandalp.schedule.domain.entity.TempCategory;
import com.gandalp.gandalp.schedule.domain.repository.ScheduleRepository;
import com.gandalp.gandalp.schedule.domain.repository.ScheduleTempRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleTempRepository scheduleTempRepository;
    private final NurseRepository nurseRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommonCodeRepository commonCodeRepository;

    public OffScheduleTempResponseDto createOffSchecule(OffScheduleRequestDto scheduleRequestDto) {
        Optional<Nurse> nurseOpt = nurseRepository.findByEmail(scheduleRequestDto.getEmail());
        if (nurseOpt.isEmpty()) {
            throw new IllegalArgumentException("간호사를 찾을 수 없습니다.");
        }

        Nurse nurse = nurseOpt.get();
        LocalDateTime start = scheduleRequestDto.getStartTime().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime end = scheduleRequestDto.getEndTime().truncatedTo(ChronoUnit.HOURS);

        System.out.println(start + "\n" + end);

        // 시간 겹치는 기존 스케줄 존재 여부 확인
        boolean hasConflict = !scheduleTempRepository
                .findOverlappingTempSchedules(nurse.getId(), start, end)
                .isEmpty()
                || !scheduleRepository
                .findOverlappingSchedules(nurse.getId(), start, end)
                .isEmpty();

        if (hasConflict) {
            throw new IllegalStateException("해당 시간대에 이미 스케줄이 존재합니다.");
        }

        // 저장
        ScheduleTemp scheduleTemp = ScheduleTemp.builder()
                .nurse(nurse)
                .category(TempCategory.WAITING_OFF)
                .content(scheduleRequestDto.getContent())
                .startTime(start)
                .endTime(end)
                .build();

        scheduleTempRepository.save(scheduleTemp);

        return OffScheduleTempResponseDto.builder()
                .offScheduleTempId(scheduleTemp.getId())
                .nurseName(nurse.getName())
                .content(scheduleTemp.getContent())
                .startTime(scheduleTemp.getStartTime())
                .endTime(scheduleTemp.getEndTime())
                .build();
    }



    public NurseResponseDto checkPassword(String password, String email) {
        Optional<Nurse> nurse = nurseRepository.findByPasswordAndEmail(password, email);
        NurseResponseDto nurseResponseDto = null;
        if (nurse.isPresent() && passwordEncoder.matches(password, nurse.get().getPassword())) {
            nurseResponseDto = NurseResponseDto.builder()
                    .name(nurse.get().getName())
                    .email(nurse.get().getEmail())
                    .build();
        } else {
            throw new RuntimeException("nurse is empty or password is wrong");
        }
        return nurseResponseDto;
    }

    public void deleteOffScheduleTemp(Long scheduleTempId) {
        try {
            scheduleTempRepository.deleteById(scheduleTempId);
        } catch (Exception e) {
            throw new RuntimeException("승인 대기 중 오프 조회 실패");
        }
    }

    public List<OffScheduleTempResponseDto> getOffScheduleTemp(String email) {

        try {
            List<ScheduleTemp> scheduleTemps = scheduleTempRepository.findAllByNurseEmail(email);

            // 변환
            List<OffScheduleTempResponseDto> responseDtos = scheduleTemps.stream()
                    .map(scheduleTemp -> OffScheduleTempResponseDto.builder()
                            .offScheduleTempId(scheduleTemp.getId())
                            .nurseName(scheduleTemp.getNurse().getName())
                            .category(scheduleTemp.getCategory())
                            .content(scheduleTemp.getContent())
                            .startTime(scheduleTemp.getStartTime())
                            .endTime(scheduleTemp.getEndTime())
                            .build()
                    )
                    .collect(Collectors.toList());

            return responseDtos;

        } catch (Exception e) {
            throw new RuntimeException("승인 대기 중 오프 조회 실패");
        }
    }

    public OffScheduleResponseDto acceptOff(Long scheduleTempId) {

        Optional<ScheduleTemp> scheduleTemp = scheduleTempRepository.findById(scheduleTempId);

        if(scheduleTemp.isPresent()) {

            if(scheduleTemp.get().getCategory() != TempCategory.WAITING_OFF) {
                throw new RuntimeException("이미 처리되었습니다.");
            }

            scheduleTemp.get().acceptedOff();

            Schedule schedule = Schedule.builder()
                    .nurse(scheduleTemp.get().getNurse())
                    .category(Category.ACCEPTED_OFF)
                    .content(scheduleTemp.get().getContent())
                    .startTime(scheduleTemp.get().getStartTime())
                    .endTime(scheduleTemp.get().getEndTime())
                    .build();

            scheduleRepository.save(schedule);

            System.out.println(schedule.getCategory());

            Optional<String> codeLabel = commonCodeRepository.findCodeLabelByCodeGroupAndCodeValue("SCHEDULE_CATEGORY",String.valueOf(schedule.getCategory()));

            if(codeLabel.isEmpty()) {
                throw new RuntimeException("Code Label is Empty");
            }

            OffScheduleResponseDto offScheduleResponseDto = OffScheduleResponseDto.builder()
                    .offScheduleId(schedule.getId())
                    .nurseId(schedule.getNurse().getId())
                    .codeLabel(codeLabel.get())
                    .content(schedule.getContent())
                    .startTime(schedule.getStartTime())
                    .endTime(schedule.getEndTime())
                    .build();
            return offScheduleResponseDto;

        } else {
            throw new RuntimeException("scheduleTemp is empty");
        }
    }
}
