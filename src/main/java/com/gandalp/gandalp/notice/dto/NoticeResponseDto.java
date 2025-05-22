package com.gandalp.gandalp.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class NoticeResponseDto {
	private Long noticeId;

	private String codeLabel;
	private String content;
}
