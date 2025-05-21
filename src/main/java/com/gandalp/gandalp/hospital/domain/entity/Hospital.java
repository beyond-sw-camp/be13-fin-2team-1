package com.gandalp.gandalp.hospital.domain.entity;

import com.gandalp.gandalp.common.entity.BaseEntity;

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
public class Hospital extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hospital-id")
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 100)
	private String address;

	@Column(nullable = true)
	private Double latitude;

	@Column(nullable = true)
	private Double longitude;

	@Column(nullable = false)
	private int totalErCount;

	@Column(nullable = false)
	private int availableErCount;

	@Column(nullable = false, length = 20)
	private String phoneNumber;

	public int updateAvailableErCount(int count) {
		if( count < 0 )
			throw new IllegalArgumentException("응급실 병상 수가 0개 미만일 수 없습니다. ");

		this.availableErCount = count;
		return count;
	}

	public void updateGeoCode(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
