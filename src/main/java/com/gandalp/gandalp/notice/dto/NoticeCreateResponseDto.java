package com.gandalp.gandalp.notice.dto;

import com.gandalp.gandalp.hospital.domain.entity.Department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class NoticeCreateResponseDto {

	private Long id;
	private String category;
	private String content;
	private String departmentName;

}
