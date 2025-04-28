package com.gandalp.gandalp.member.domain.entity;

import com.gandalp.gandalp.hospital.domain.entity.Department;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nurse-id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department-id")
	private Department department;

	@Column(nullable = false, length = 50)
	private String email;

	@Column(nullable = false, length = 10)
	private String password;

	private Status workingStatus;

	@Column(nullable = false)
	private int workingCount = 0;

	@Column(nullable = false)
	private int surgeryCount = 0;

	@Column(nullable = false)
	private int offCount = 0;
}
