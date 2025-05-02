package com.gandalp.gandalp.common;

import com.gandalp.gandalp.auth.model.dto.CustomUserDetails;
import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.member.domain.entity.Type;
import com.gandalp.gandalp.member.domain.repository.MemberRepository;
import com.gandalp.gandalp.member.domain.repository.NurseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final NurseRepository nurseRepository;


    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject == null) {
            return false;
        }

        Long resourceId = (Long) targetDomainObject;


        switch (permission.toString().toUpperCase()) {
            case "HEAD_NURSE_ACCESS":
                return canAccessNurseAsHeadNurse(resourceId, authentication);
            case "NURSE_ACCESS":
                return canAccessNurseAsNurse(resourceId, authentication);
            default:
                return false;
        }
    }

    // 수간호사가 수정, 삭제하는 간호사가 같은 병원, 부서인지 검증하는 코드
    private boolean canAccessNurseAsHeadNurse(Long nurseId, Authentication authentication) {
        // 인증 정보 검증
        if (authentication == null || authentication.getPrincipal() == null) return false;

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) return false;

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        Member loginMember = userDetails.getMember();

        // 수간호사 권한 확인
        if (loginMember.getType() != Type.HEAD_NURSE) return false;

        // 로그인 멤버의 병원/부서 조회
        Department loginDept = loginMember.getDepartment();
        Long loginHospitalId = loginMember.getHospital().getId();

        // 대상 간호사 조회
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 간호사를 찾을 수 없습니다."));

        Department targetDept = nurse.getDepartment();
        Long targetHospitalId = targetDept.getHospital().getId();

        // 병원, 부서 일치 여부 확인
        return loginDept.getId().equals(targetDept.getId()) &&
                loginHospitalId.equals(targetHospitalId);
    }


    // 간호사 A 가 같은 부서내의 간호사들 조회
    private boolean canAccessNurseAsNurse(Long nurseId, Authentication authentication) {
        // 인증 정보 검증
        if (authentication == null || authentication.getPrincipal() == null) return false;

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) return false;

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        Member loginMember = userDetails.getMember();

        // 간호사, 수간호사 권한 확인
        if (loginMember.getType() != Type.HEAD_NURSE && loginMember.getType() != Type.NURSE )
            return false;

        // 로그인 멤버의 병원/부서 조회
        Department loginDept = loginMember.getDepartment();
        Long loginHospitalId = loginMember.getHospital().getId();

        // 대상 간호사 조회
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 간호사를 찾을 수 없습니다."));

        Department targetDept = nurse.getDepartment();
        Long targetHospitalId = targetDept.getHospital().getId();

        // 병원, 부서 일치 여부 확인
        return loginDept.getId().equals(targetDept.getId()) &&
                loginHospitalId.equals(targetHospitalId);
    }



    // 필요하지 않은 경우 false 반환
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
