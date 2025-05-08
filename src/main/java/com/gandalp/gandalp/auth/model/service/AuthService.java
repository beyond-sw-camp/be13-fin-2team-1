package com.gandalp.gandalp.auth.model.service;

import com.gandalp.gandalp.auth.model.dto.JoinRequestDto;
import com.gandalp.gandalp.auth.model.dto.LoginRequestDto;
import com.gandalp.gandalp.auth.model.dto.TokenResponseDto;
import com.gandalp.gandalp.member.domain.entity.Member;

public interface AuthService {


    void join(JoinRequestDto dto);

    TokenResponseDto login(LoginRequestDto dto);

    void logout(String bearerToken);

    TokenResponseDto refresh(String bearerToken);

    Member getLoginMember();

    void validateDuplicateEmail(String email);

}
