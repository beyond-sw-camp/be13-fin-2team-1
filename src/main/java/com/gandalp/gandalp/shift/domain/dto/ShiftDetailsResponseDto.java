package com.gandalp.gandalp.shift.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDetailsResponseDto {

    @NotNull
    private Long boardId;

    @NotNull
    private String content;

    @NotNull
    private String boardStatus;

    @NotNull
    List<CommentResponseDto> comments;
}
