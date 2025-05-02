package com.gandalp.gandalp.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank
    private final String accountId;

    @NotBlank
    private final String password;
}
