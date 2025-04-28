package com.gandalp.gandalp.hospital.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hospital {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hospital-id")
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 100)
	private String address;

	@Column(nullable = false)
	private int erCount;

	@Column(nullable = false, length = 20)
	private String phoneNumber;
}
