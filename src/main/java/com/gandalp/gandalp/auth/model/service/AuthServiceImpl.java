package com.gandalp.gandalp.auth.model.service;

import com.gandalp.gandalp.auth.jwt.JwtTokenProvider;
import com.gandalp.gandalp.auth.model.dto.JoinRequestDto;
import com.gandalp.gandalp.auth.model.dto.LoginRequestDto;
import com.gandalp.gandalp.auth.model.dto.TokenResponseDto;
import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.hospital.domain.entity.Hospital;
import com.gandalp.gandalp.hospital.domain.repository.DepartmentRepository;
import com.gandalp.gandalp.hospital.domain.repository.HospitalRepository;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final DepartmentRepository departmentRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    @Transactional
    public void join(JoinRequestDto dto) {
        String accountId = dto.getAccountId();
        String password = dto.getPassword();

        // 아이디 중복 체크
        if (memberRepository.findByAccountId(accountId).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 계정입니다.");
        }


        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);


        // 병원 엔티티
        Hospital hospital = hospitalRepository.findByName(dto.getHospital()).orElseThrow(
                () -> new EntityNotFoundException("해당하는 병원이 없습니다.")
        );


        // 부서
        Department department = departmentRepository.findByNameAndHospital(dto.getDepartment(), hospital).orElseThrow(
                () -> new IllegalArgumentException("해당하는 부서가 없습니다.")
        );


        Member member = Member.builder()
                .accountId(accountId)
                .password(encodedPassword)
                .type(dto.getType()) // 기본값: PARAMEDIC
                .hospital(hospital)
                .department(department)
                .build();

        memberRepository.save(member);


        log.info("회원가입 완료 - accountId: {}", accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto dto) {
        String accountId = dto.getAccountId();
        String password = dto.getPassword();

        Member member = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 계정이 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getAccountId(), member.getType().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getAccountId());

        return new TokenResponseDto(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public void logout(String bearerToken) {
        String accessToken = jwtTokenProvider.resolveToken(bearerToken)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        jwtTokenProvider.addBlacklist(accessToken);
        jwtTokenProvider.deleteRefreshToken(accessToken);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto refresh(String bearerToken) {
        String refreshToken = jwtTokenProvider.resolveToken(bearerToken)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        Member member = memberRepository.findByAccountId(jwtTokenProvider.getUserName(refreshToken))
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        String accessToken = jwtTokenProvider.createAccessToken(member.getAccountId(), member.getType().name());

        return new TokenResponseDto(accessToken, refreshToken);
    }
}
