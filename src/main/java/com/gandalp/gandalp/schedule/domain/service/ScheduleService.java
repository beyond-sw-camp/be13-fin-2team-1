package com.gandalp.gandalp.schedule.domain.service;

import com.gandalp.gandalp.member.domain.dto.NurseResponseDto;
import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.member.domain.repository.NurseRepository;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleRequestDto;
import com.gandalp.gandalp.schedule.domain.dto.OffScheduleTempResponseDto;
import com.gandalp.gandalp.schedule.domain.entity.Category;
import com.gandalp.gandalp.schedule.domain.entity.Schedule;
import com.gandalp.gandalp.schedule.domain.entity.ScheduleTemp;
import com.gandalp.gandalp.schedule.domain.entity.TempCategory;
import com.gandalp.gandalp.schedule.domain.repository.ScheduleRepository;
import com.gandalp.gandalp.schedule.domain.repository.ScheduleTempRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleTempRepository scheduleTempRepository;
    private final NurseRepository nurseRepository;
    private final PasswordEncoder passwordEncoder;

    public OffScheduleTempResponseDto createOffSchecule(OffScheduleRequestDto scheduleRequestDto) {

        Optional<Nurse> nurse = nurseRepository.findByEmail(scheduleRequestDto.getEmail());
        ScheduleTemp scheduleTemp = null;
        OffScheduleTempResponseDto offScheduleTempResponseDto = null;
        if (nurse.isPresent()) {

            scheduleTemp = ScheduleTemp.builder()
                                .nurse(nurse.get())
                                .category(TempCategory.WAITING_OFF)
                                .content(scheduleRequestDto.getContent())
                                .startTime(scheduleRequestDto.getStartTime())
                                .endTime(scheduleRequestDto.getEndTime())
                                .build();

            scheduleTempRepository.save(scheduleTemp);

            offScheduleTempResponseDto = OffScheduleTempResponseDto.builder()
                    .offScheduleTempId(scheduleTemp.getId())
                    .nurseName(scheduleTemp.getNurse().getName())
                    .content(scheduleTemp.getContent())
                    .startTime(scheduleTemp.getStartTime())
                    .endTime(scheduleTemp.getEndTime())
                    .build();
        }
        return offScheduleTempResponseDto != null ? offScheduleTempResponseDto : null;
    }



    public NurseResponseDto checkPassword(String password, String email) {


        // Optional<Nurse> nurse = nurseRepository.findByPasswordAndEmail(password, email);
        Optional<Nurse> nurse = nurseRepository.findByEmail(email);

        NurseResponseDto nurseResponseDto = null;
        if (nurse.isPresent() && passwordEncoder.matches(password, nurse.get().getPassword())) {
            nurseResponseDto = NurseResponseDto.builder()
                    .name(nurse.get().getName())
                    .email(nurse.get().getEmail())
                    .build();
        } else {
            return null;
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
            return Collections.emptyList(); // 예외 발생 시 빈 리스트 반환
        }
    }
}
