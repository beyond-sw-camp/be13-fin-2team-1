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

    private String hospitalName;

    private String departmentName;

    private String accountId;

    private Type type;

    public MemberResponseDto(Member member){
        this.id = member.getId();
        this.hospitalName = member.getHospital() != null
                ? member.getHospital().getName()
                : null;
        this.departmentName = member.getDepartment() != null
                ? member.getDepartment().getName()
                : null;
        this.accountId = member.getAccountId();
        this.type = member.getType();
    }

}
