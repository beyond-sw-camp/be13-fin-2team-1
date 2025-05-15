package com.gandalp.gandalp.shift.domain.service;

import com.gandalp.gandalp.shift.domain.dto.CommentCreateRequestDto;
import com.gandalp.gandalp.shift.domain.dto.CommentResponseDto;
import com.gandalp.gandalp.shift.domain.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {
    // 댓글 작성
    CommentResponseDto createComment(CommentCreateRequestDto shiftCreateRequestDto);

    // 댓글 수정
    CommentResponseDto updateComment(CommentUpdateDto shiftUpdateDto);

    // 댓글 조회
//    List<CommentResponseDto> findByBoardId(Long boardId);

    // 댓글 삭제
    void deleteComment(Long boardId);
}
