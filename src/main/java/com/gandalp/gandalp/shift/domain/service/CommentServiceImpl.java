package com.gandalp.gandalp.shift.domain.service;

import com.gandalp.gandalp.auth.model.service.AuthService;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.repository.MemberRepository;
import com.gandalp.gandalp.shift.domain.dto.CommentCreateRequestDto;
import com.gandalp.gandalp.shift.domain.dto.CommentResponseDto;
import com.gandalp.gandalp.shift.domain.dto.CommentUpdateDto;
import com.gandalp.gandalp.shift.domain.entity.Board;
import com.gandalp.gandalp.shift.domain.entity.Comment;
import com.gandalp.gandalp.shift.domain.repository.CommentRepository;
import com.gandalp.gandalp.shift.domain.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ShiftRepository shiftRepository;

    private final AuthService authService;

    // 댓글 C
    @Override
    public CommentResponseDto createComment(CommentCreateRequestDto commentCreateRequestDto) {

        Member member = authService.getLoginMember();

        Long boardId = commentCreateRequestDto.getBoardId();
        Board board = shiftRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        Comment comment = Comment.builder()
                .content(commentCreateRequestDto.getContent())
                .board(board)
                .member(member)
                .build();

        commentRepository.save(comment);

        return new CommentResponseDto(comment);

    }

    // 댓글 U

    @Override
    public CommentResponseDto updateComment(CommentUpdateDto commentUpdateDto) {

        Long memberId = commentUpdateDto.getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        Long boardId = commentUpdateDto.getBoardId();
        Board board = shiftRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        Long commentId = commentUpdateDto.getCommentId();
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        comment.update(commentUpdateDto);
        return new CommentResponseDto(comment);
    }


    // 댓글 D
    @Override
    public void deleteComment(Long commentId) {
        // 댓글, 게시판, 회원이 존재하는지 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다")
        );

        commentRepository.deleteById(commentId);
    }
}
