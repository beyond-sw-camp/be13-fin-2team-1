package com.gandalp.gandalp.shift.domain.dto;

import com.gandalp.gandalp.common.entity.CommonCode;
import com.gandalp.gandalp.common.repository.CommonCodeRepository;
import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.shift.domain.entity.Board;
import com.gandalp.gandalp.shift.domain.entity.BoardStatus;
import com.gandalp.gandalp.shift.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftResponseDto {

    private Long boardId;
    private Long memberId;
    private Long departmentId;
    private String boardStatus;      // code_value
    private String boardStatusLabel; // code_label
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<CommentResponseDto> comments; // 엔티티 대신 DTO


    public ShiftResponseDto(Board board) {
        this.boardId = board.getId();
        this.memberId = board.getMember().getId();
        this.departmentId = board.getDepartment().getId();
        this.content = board.getContent();
        this.boardStatus = board.getBoardStatus();
        this.createdAt = board.getCreatedAt();
        this.createdBy = board.getCreatedBy();
        this.updatedAt = board.getUpdatedAt();
        this.updatedBy = board.getUpdatedBy();
        // 엔티티 -> DTO 변환 예시
        this.comments = board.getComments() != null
                ? board.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList())
                : new ArrayList<>();

    }


    public ShiftResponseDto(Long boardId, String content, String boardStatus, String boardStatusLabel, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.content = content;
        this.boardStatus = boardStatus;
        this.boardStatusLabel = boardStatusLabel;
        this.createdAt = createdAt;
    }


    // QueryDSL 등에서 쓰는 생성자
    public ShiftResponseDto(Long boardId, String content, String boardStatus, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.content = content;
        this.boardStatus = boardStatus;
        this.createdAt = createdAt;
    }


}
