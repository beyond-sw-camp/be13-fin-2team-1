package com.gandalp.gandalp.member.domain.service;

import com.gandalp.gandalp.member.domain.dto.MemberResponseDto;
import com.gandalp.gandalp.member.domain.dto.MemberUpdateDto;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.entity.MemberSearchOption;
import com.gandalp.gandalp.member.domain.entity.Type;
import com.gandalp.gandalp.member.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Page<MemberResponseDto> getAllMembers(MemberSearchOption option, String keyword, Type type, Pageable pageable){


        // 기본 전체 조회

        // 검색어가 있는데 검색 옵션이 없는 경우 검색이 안됨
        if (keyword != null && option == null) {
            throw new IllegalArgumentException("검색 옵션을 선택해 주십시오.");
        }

        Page<MemberResponseDto> searchResults = memberRepository.searchMembers( keyword, type, option, pageable);

        // 검색 결과가 없는 경우 예외 처리
        if (searchResults.isEmpty()) {
            throw new EntityNotFoundException("멤버가 존재하지 않습니다.");
        }

        return searchResults;
    }

    @Transactional
    public MemberResponseDto updateMember(Long memberId, MemberUpdateDto updateDto){

        // 1. member 조회
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new EntityNotFoundException("해당하는 회원이 존재하지 않습니다.")
        );

        // 2. update
        member.update(updateDto);

        return new MemberResponseDto(member);
    }

    @Transactional
    public void deleteMember(Long memberId){

        // 1. 회원이 존재하는지
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new EntityNotFoundException("해당하는 회원이 존재하지 않습니다.")
        );


        memberRepository.deleteById(member.getId());

    }




}
