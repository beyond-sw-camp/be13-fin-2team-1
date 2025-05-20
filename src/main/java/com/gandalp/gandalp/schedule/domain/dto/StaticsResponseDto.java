package com.gandalp.gandalp.schedule.domain.dto;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.member.domain.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
public class StaticsResponseDto {

	private Long nurseId;
	private String nurseName;

	// dto에서도 기본값 0으로 세팅해주기
	@Builder.Default
	private int dayCount = 0;
	@Builder.Default
	private int eveningCount = 0;
	@Builder.Default
	private int nightCount = 0;
	@Builder.Default
	private int offCount = 0;


	private int surgeryCount;
}
