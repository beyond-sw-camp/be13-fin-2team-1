package com.gandalp.gandalp.notice.entity;

import com.gandalp.gandalp.common.entity.BaseEntity;
import com.gandalp.gandalp.hospital.domain.entity.Department;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Notice extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notice-id")
	private Long id;

	// dept 1 - notice many
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department-id")
	private Department department;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Category category;

	@Column(nullable = false)
	private String content;


}
