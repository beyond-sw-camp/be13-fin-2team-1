package com.gandalp.gandalp.schedule.domain.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SurgeryScheduleResponseDto {

	private Long surgeryScheduleId;

	private Long roomId;

	private String content;

	private List<String> names;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	public void setNurseNames(List<String> nurseNames) {
		this.names = nurseNames;
	}

	public SurgeryScheduleResponseDto(Long surgeryScheduleId, Long roomId, String content,
		LocalDateTime startTime, LocalDateTime endTime) {
		this.surgeryScheduleId = surgeryScheduleId;
		this.roomId = roomId;
		this.content = content;
		this.names = new ArrayList<>();
		this.startTime = startTime;
		this.endTime = endTime;
	}

}
