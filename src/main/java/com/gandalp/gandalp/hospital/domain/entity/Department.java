package com.gandalp.gandalp.hospital.domain.entity;

import com.gandalp.gandalp.common.entity.BaseEntity;

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
public class Department extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospital-id")
	private Hospital hospital;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private int nurseCount = 0;


	public void countUp(){
		this.nurseCount++;
	}

	public void countDown(){
		this.nurseCount--;
	}
}
