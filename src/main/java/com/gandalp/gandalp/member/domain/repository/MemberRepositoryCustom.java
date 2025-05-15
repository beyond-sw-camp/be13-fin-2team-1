package com.gandalp.gandalp.member.domain.repository;

import com.gandalp.gandalp.member.domain.dto.MemberResponseDto;
import com.gandalp.gandalp.member.domain.entity.MemberSearchOption;
import com.gandalp.gandalp.member.domain.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<MemberResponseDto> searchMembers(String keyword, Type type, MemberSearchOption option, Pageable pageable);

}
