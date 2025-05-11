package com.gandalp.gandalp.schedule.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StaticsUpdateDto {


	private int year;
	private int month;

	private int dayCount;
	private int eveningCount;
	private int nightCount;
	private int surgeryCount;
	private int offCount;
}
