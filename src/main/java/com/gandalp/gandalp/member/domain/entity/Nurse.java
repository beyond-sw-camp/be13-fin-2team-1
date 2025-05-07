package com.gandalp.gandalp.member.domain.entity;

import com.gandalp.gandalp.hospital.domain.entity.Department;

import com.gandalp.gandalp.member.domain.dto.NurseUpdateDto;
import com.gandalp.gandalp.schedule.domain.entity.SurgerySchedule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Nurse {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nurse-id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department-id")
	private Department department;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, length = 50)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private Status workingStatus;

	public void update(NurseUpdateDto updateDto){
		this.name = updateDto.getName();
		this.email = updateDto.getEmail();
		this.password = updateDto.getPassword();
	}

	public void updateWorkingStatus(Status workingStatus) {
		this.workingStatus = workingStatus;
	}
}
