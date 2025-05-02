package com.gandalp.gandalp.member.domain.service;

import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.member.domain.dto.NurseRequestDto;
import com.gandalp.gandalp.member.domain.dto.NurseResponseDto;
import com.gandalp.gandalp.member.domain.dto.NurseUpdateDto;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.member.domain.repository.MemberRepository;
import com.gandalp.gandalp.member.domain.repository.NurseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HeadNurseService {

    private final PasswordEncoder passwordEncoder;
    private final NurseRepository nurseRepository;
    private final MemberRepository memberRepository;

    // 수간호사가 간호사 생성
    @Transactional
    public void createNurse(NurseRequestDto reqDto, Member loginMember){

        // 1. login 한 회원이 member repo 에 존재하는지 검증
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(
                () -> new EntityNotFoundException("해당하는 회원이 없습니다.")
        );


        // 2. 부서 추출
        Department department = member.getDepartment();


        // 3. nurse 이메일 중복 체크
        if (nurseRepository.findByEmail(reqDto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        // 4. 비밀번호 암호화
        String password = passwordEncoder.encode(reqDto.getPassword());


        // 5. nurse 생성
        Nurse nurse = Nurse.builder()
                            .department(department)
                            .name(reqDto.getName())
                            .email(reqDto.getEmail())
                            .password(password)
                            .build();


        nurseRepository.save(nurse);

        log.info("간호사 생성 완료 ~~");

    }


    // 수간호사가 간호사 수정
    @Transactional
    public NurseResponseDto updateNurse(Long nurseId, NurseUpdateDto dto){


        // 1. nurse 존재하는지 검증
        Nurse nurse = nurseRepository.findById(nurseId).orElseThrow(
                ()-> new EntityNotFoundException("해당하는 간호사가 존재하지 않습니다. ")
        );

        // 2. 수정
        nurse.update(dto);

        return new NurseResponseDto(nurse);
    }



    // 수간호사가 간호사 삭제
    @Transactional
    public void deleteNurse(Long nurseId) {


        Nurse nurse = nurseRepository.findById(nurseId).orElseThrow(
                () -> new EntityNotFoundException("해당하는 간호사가 존재하지 않습니다.")
        );


        nurseRepository.deleteById(nurseId);
    }


    // 모든 간호사 조회 (수간호사 모두 가능) -> 페이징 처리
    public Page<NurseResponseDto> getAll(String keyword, Pageable pageable, Long departmentId){

        // 검색어가 있는데 검색 옵션을 선택하지 않은 경우 검색이 안됨

        Page<NurseResponseDto> nurseList = nurseRepository.getAll(keyword, pageable, departmentId);

        return nurseList;
    }



//    // 간호사 단 건 조회
//    public NurseResponseDto getOneNurse(Long nurseId){
//
//        // 1. 간호사 조회
//        Nurse nurse = nurseRepository.findById(nurseId).orElseThrow(
//                () -> new EntityNotFoundException("해당하는 간호사가 존재하지 않습니다.")
//        );
//
//
//        return new NurseResponseDto(nurse);
//    }









}
