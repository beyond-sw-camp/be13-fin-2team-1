package com.gandalp.gandalp.member;

import com.gandalp.gandalp.member.domain.dto.MemberResponseDto;
import com.gandalp.gandalp.member.domain.dto.MemberUpdateDto;
import com.gandalp.gandalp.member.domain.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // Admin 만 수정 삭제
    @Operation(summary = "회원 정보 수정", description = "관리자가 회원의 정보를 수정합니다.")
    @PostMapping("/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable Long memberId, MemberUpdateDto updateDto){

        MemberResponseDto memberResponseDto = memberService.updateMember(memberId, updateDto);

        return ResponseEntity.ok(memberResponseDto);
    }


    @Operation(summary = "회원 삭제", description = "관리자가 특정 회원을 삭제합니다.")
    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId){
        memberService.deleteMember(memberId);

        return ResponseEntity.ok("회원 삭제 완료");
    }

}
