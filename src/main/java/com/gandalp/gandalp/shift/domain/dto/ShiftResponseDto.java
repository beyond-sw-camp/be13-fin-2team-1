package com.gandalp.gandalp.shift.domain.dto;

import com.gandalp.gandalp.common.entity.CommonCode;
import com.gandalp.gandalp.common.repository.CommonCodeRepository;
import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.shift.domain.entity.Board;
import com.gandalp.gandalp.shift.domain.entity.BoardStatus;
import com.gandalp.gandalp.shift.domain.entity.Comment;
import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftResponseDto {

    private Long boardId;
    private Long memberId;
    private Long departmentId;
    private String boardStatusLabel; // code_label
    private String content;
    private LocalDateTime updatedAt;
    private List<CommentResponseDto> comments; // 엔티티 대신 DTO

    public ShiftResponseDto(Board board, String codeLable) {
        this.boardId = board.getId();
        this.content = board.getContent();
        this.memberId = board.getMember() != null ? board.getMember().getId() : null;
        this.departmentId = board.getDepartment() != null ? board.getDepartment().getId() : null;
        this.boardStatusLabel = codeLable;
        this.updatedAt = board.getUpdatedAt();
        this.comments = board.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    public ShiftResponseDto(Long boardId, String boardStatusLabel, String content, LocalDateTime updatedAt) {
        this.boardId = boardId;
        this.boardStatusLabel = boardStatusLabel;
        this.content = content;
        this.updatedAt = updatedAt;
    }


}
