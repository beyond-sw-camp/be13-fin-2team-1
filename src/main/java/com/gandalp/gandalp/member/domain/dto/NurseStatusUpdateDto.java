package com.gandalp.gandalp.member.domain.dto;

import com.gandalp.gandalp.member.domain.entity.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NurseStatusUpdateDto {

	@NotBlank
	private String email;

	@NotBlank
	private String password;

	@NotNull
	private Status workingStatus;
}
