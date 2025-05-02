package com.gandalp.gandalp.member.domain.service;

import com.gandalp.gandalp.member.domain.dto.MemberResponseDto;
import com.gandalp.gandalp.member.domain.dto.MemberUpdateDto;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponseDto updateMember(Long memberId, MemberUpdateDto updateDto){

        // 1. member 조회
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new EntityNotFoundException("해당하는 회원이 존재하지 않습니다.")
        );

        // 2. update
        member.update(updateDto);

        return new MemberResponseDto(member);
    }

    public void deleteMember(Long memberId){

        // 1. 회원이 존재하는지
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new EntityNotFoundException("해당하는 회원이 존재하지 않습니다.")
        );


        memberRepository.deleteById(member.getId());

    }




}
