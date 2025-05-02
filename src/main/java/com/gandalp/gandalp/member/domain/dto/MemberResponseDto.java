package com.gandalp.gandalp.member.domain.dto;

import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.hospital.domain.entity.Hospital;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {

    private Long id;

    private Hospital hospital;

    private Department department;

    private String accountId;

    private String password;

    private Type type;

    public MemberResponseDto(Member member){
        this.id = member.getId();
        this.hospital = member.getHospital();
        this.department = member.getDepartment();
        this.accountId = member.getAccountId();
        this.password = member.getPassword();
        this.type = member.getType();
    }

}
