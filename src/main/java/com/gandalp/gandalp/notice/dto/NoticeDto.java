package com.gandalp.gandalp.notice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class NoticeDto {

	@NotNull
	private String content;
}
