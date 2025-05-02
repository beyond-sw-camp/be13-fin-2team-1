package com.gandalp.gandalp.common.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Getter
@ToString
@MappedSuperclass
public class AuditableEntity extends BaseEntity{

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}