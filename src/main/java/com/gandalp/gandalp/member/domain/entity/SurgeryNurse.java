package com.gandalp.gandalp.member.domain.entity;

import com.gandalp.gandalp.schedule.domain.entity.SurgerySchedule;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SurgeryNurse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse-id")
    private Nurse nurse;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery-schedule-id")
    private SurgerySchedule surgerySchedule;

}
