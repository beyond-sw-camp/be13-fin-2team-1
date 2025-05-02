package com.gandalp.gandalp.member.domain.entity;

import com.gandalp.gandalp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class NurseStatistics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status-id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse-id")
    private Nurse nurse;


    @Column(nullable = false)
    private int workingCount = 0;

    @Column(nullable = false)
    private int surgeryCount = 0;

    @Column(nullable = false)
    private int offCount = 0;

}
