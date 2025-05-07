package com.gandalp.gandalp.member;

import java.util.List;

import com.gandalp.gandalp.auth.model.dto.CustomUserDetails;
import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.member.domain.dto.NurseCurrentStatusDto;
import com.gandalp.gandalp.member.domain.dto.NurseRequestDto;
import com.gandalp.gandalp.member.domain.dto.NurseResponseDto;
import com.gandalp.gandalp.member.domain.dto.NurseStatusUpdateDto;
import com.gandalp.gandalp.member.domain.dto.NurseUpdateDto;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.service.HeadNurseService;
import com.gandalp.gandalp.member.domain.service.NurseService;
import com.gandalp.gandalp.schedule.domain.service.ScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/nurses")
@RequiredArgsConstructor
public class NurseController {

    private final NurseService nurseService;
    private final HeadNurseService headNurseService;
    private final ScheduleService scheduleService;

    // 간호사 생성
    @Operation(summary = "간호사 생성", description = "수간호사가 새로운 간호사를 생성합니다.")
    @PostMapping
    @PreAuthorize("hasRole('HEAD_NURSE')")
    public ResponseEntity<String> createNurse(@RequestBody NurseRequestDto dto, Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails)auth.getPrincipal();
        Member loginMember = userDetails.getMember();

        // 나중에 security 엔드포인트 추가하깅

        headNurseService.createNurse(dto, loginMember);

        return ResponseEntity.ok("간호사 생성");
    }

    // 간호사 수정
    @Operation(summary = "간호사 수정", description = "수간호사가 간호사 정보를 수정합니다.")
    @PostMapping("/{nurseId}")
    @PreAuthorize("hasRole('HEAD_NURSE') and hasPermission(#nurseId, 'HEAD_NURSE_ACCESS')")
    public ResponseEntity<NurseResponseDto> updateNurse(@PathVariable Long nurseId, @RequestBody NurseUpdateDto dto) {

        // 나중에 security 엔드포인트 추가하깅

        NurseResponseDto resDto = headNurseService.updateNurse(nurseId, dto);

        return ResponseEntity.ok(resDto);
    }

    // 간호사 삭제
    @Operation(summary = "간호사 삭제", description = "수간호사가 간호사를 삭제합니다.")
    @DeleteMapping("/{nurseId}")
    @PreAuthorize("hasRole('HEAD_NURSE') and hasPermission(#nurseId, 'HEAD_NURSE_ACCESS')")
    public ResponseEntity<String> deleteNurse(@PathVariable Long nurseId) {

        headNurseService.deleteNurse(nurseId);
        return ResponseEntity.status(HttpStatus.OK).body("간호사가 삭제되었습니다.");
    }

    //간호사 전체 조회
    @Operation(summary = "간호사 전체 조회", description = "수간호사가 소속된 부서의 간호사들을 조회합니다.")
    @GetMapping
    @PreAuthorize("hasRole('HEAD_NURSE')")
    public ResponseEntity<Page<NurseResponseDto>> getAllNurses(
        @RequestParam(required = false) String keyword,
        @PageableDefault(size = 10, page = 0) Pageable pageable,
        Authentication auth
    ) {

        CustomUserDetails userDetails = (CustomUserDetails)auth.getPrincipal();
        Long departmentId = userDetails.getMember().getDepartment().getId();

        Page<NurseResponseDto> allNurses = headNurseService.getAll(keyword, pageable, departmentId);

        return ResponseEntity.ok(allNurses);
    }

    // 간호사들의 현재 상태 조회
    // 로그인한 계정의 과를 가지고 와서 모든 간호사들의 상태 조회 list 로 반환
    @Operation(summary = "간호사들 현재 상태 조회", description = "해당 과의 모든 간호사들의 현재 근무상태를 조회합니다.")
    @GetMapping("/status")
    public ResponseEntity<List<NurseCurrentStatusDto>> getNurseStatus(Authentication auth) {

        CustomUserDetails userDetails = (CustomUserDetails)auth.getPrincipal();
        if (userDetails == null)
            throw new IllegalArgumentException("로그인을 해주세요");

        Department department = userDetails.getMember().getDepartment();

        List<NurseCurrentStatusDto> status = nurseService.getNurseStatus(department);

        return ResponseEntity.ok(status);
    }

    // 간호사들의 현재 상태 수정
    // 로그인한 간호사 계정의 과를 가져와서 이메일과 비밀번호로 수정 가능
    @Operation(summary = "간호사 현재 상태 수정", description = "간호사 계정으로 로그인한 해당 과의 간호사가 자신의 상태를 수정 가능")
    @PostMapping("/status")
    public ResponseEntity<NurseCurrentStatusDto> updateNurseStatus(
        @RequestBody NurseStatusUpdateDto request,
        Authentication auth) {

        // 로그인 했는지 검증
        CustomUserDetails userDetails = (CustomUserDetails)auth.getPrincipal();
        if (userDetails == null)
            throw new IllegalArgumentException("로그인이 필요한 서비스입니다.");

        // 2. 이메일 + 비밀번호로 본인 인증
        NurseResponseDto nurse = scheduleService.checkPassword(request.getPassword(), request.getEmail());

        if (nurse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        NurseCurrentStatusDto nurseStatus = nurseService.updateNurseStatus(request.getEmail(), request.getWorkingStatus());

        return ResponseEntity.ok(nurseStatus);
    }






}
