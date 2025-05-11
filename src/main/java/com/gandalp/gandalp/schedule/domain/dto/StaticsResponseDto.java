package com.gandalp.gandalp.schedule.domain.dto;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import com.gandalp.gandalp.member.domain.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StaticsResponseDto {

	private Long nurseId;
	private String nurseName;

	private int dayCount;
	private int eveningCount;
	private int nightCount;


	private int offCount;


	private int surgeryCount;
}
