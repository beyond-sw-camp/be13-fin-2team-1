package com.gandalp.gandalp.shift.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDto {
    @NotNull
    private Long commentId;

    @NotNull
    private Long boardId;

    @NotNull
    private Long memberId;

    @NotNull
    private String content;

    @NotNull
    private LocalDateTime updatedAt;

    @NotNull
    private String updatedBy;
}
