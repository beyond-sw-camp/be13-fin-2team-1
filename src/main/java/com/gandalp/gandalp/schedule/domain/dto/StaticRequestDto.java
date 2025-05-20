package com.gandalp.gandalp.schedule.domain.dto;

import com.gandalp.gandalp.member.domain.entity.Status;
import com.gandalp.gandalp.schedule.domain.entity.SelectOption;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StaticRequestDto {

	// 지금 년도 가 항상 기본값이다.
	@Parameter(required = false)
	private Integer year;

	@Parameter(required = false)
	@Schema(description = "선택 옵션: MONTH, QUARTER, YEAR")
	private SelectOption selectOption;

	@Parameter(required = false)
	@Schema(description = "선택한 월 (1~12). selectOption이 MONTH일 때만 사용")
	private Integer month;

	@Parameter(required = false)
	@Schema(description = "선택한 분기 (1~4). selectOption이 QUARTER일 때만 사용")
	private Integer quarter;

//	@Schema(description = "근무 상태. ON / OFF / IN SURGERY")
//	private Status status;
}
