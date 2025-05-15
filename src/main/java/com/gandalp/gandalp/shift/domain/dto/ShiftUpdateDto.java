package com.gandalp.gandalp.shift.domain.dto;

import com.gandalp.gandalp.shift.domain.entity.BoardStatus;
import com.gandalp.gandalp.shift.domain.entity.Comment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftUpdateDto {
    private Long boardId;

    @NotNull
    private Long memberId;

    @NotNull
    private Long departmentId;

    @NotNull
    private String content;



}

