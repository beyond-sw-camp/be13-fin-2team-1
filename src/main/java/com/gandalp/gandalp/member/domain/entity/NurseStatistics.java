package com.gandalp.gandalp.member.domain.entity;

import com.gandalp.gandalp.common.entity.BaseEntity;
import com.gandalp.gandalp.schedule.domain.dto.StaticsResponseDto;
import com.gandalp.gandalp.schedule.domain.dto.StaticsUpdateDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(
    name = "nurse_statistics",
    uniqueConstraints = @UniqueConstraint(columnNames = {"nurse-id", "year", "month"})
)
public class NurseStatistics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistics-id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse-id")
    private Nurse nurse;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int dayCount = 0;

    @Column(nullable = false)
    private int eveningCount = 0;

    @Column(nullable = false)
    private int nightCount = 0;

    @Column(nullable = false)
    private int surgeryCount = 0;

    @Column(nullable = false)
    private int offCount = 0;


    public void updateStatic(StaticsUpdateDto dto) {
        this.year = dto.getYear();
        this.month = dto.getMonth();
        this.dayCount = dto.getDayCount();
        this.eveningCount = dto.getEveningCount();
        this.nightCount = dto.getNightCount();
        this.surgeryCount = dto.getSurgeryCount();
        this.offCount = dto.getOffCount();
    }

}
