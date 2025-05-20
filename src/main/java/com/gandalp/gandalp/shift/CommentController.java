package com.gandalp.gandalp.shift;

import com.gandalp.gandalp.shift.domain.dto.CommentCreateRequestDto;
import com.gandalp.gandalp.shift.domain.dto.CommentResponseDto;
import com.gandalp.gandalp.shift.domain.dto.CommentUpdateDto;
import com.gandalp.gandalp.shift.domain.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/shifts/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 C
    @Operation(summary = "댓글 등록", description = "댓글 등록")
    @PostMapping("/{board-id}")
    public ResponseEntity<?> createComment(
            @PathVariable("board-id") Long boardId,
            @RequestBody @Valid CommentCreateRequestDto commentCreateRequestDto) {

        try {
            commentCreateRequestDto.setBoardId(boardId); // boardId를 DTO에 세팅
            CommentResponseDto commentResponseDto = commentService.createComment(commentCreateRequestDto);
            return ResponseEntity.ok().body(commentResponseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

//    @PostMapping("/{board-id}")
//    public ResponseEntity<?> createComment(@RequestBody @Valid CommentCreateRequestDto commentCreateRequestDto) {
//
//        try {
//            CommentResponseDto commentResponseDto = commentService.createComment(commentCreateRequestDto);
//            return ResponseEntity.ok().body(commentResponseDto);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }


    //  댓글 U
    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @PutMapping("/{comment-id}")
    public ResponseEntity<?> updateComment(
            @PathVariable("comment-id") Long commentId,
            @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        try{commentUpdateDto.setCommentId(commentId); // commentId를 DTO에 세팅
            CommentResponseDto commentResponseDto = commentService.updateComment(commentUpdateDto);
            return ResponseEntity.ok().body(commentResponseDto);}
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


//    @PutMapping("/{comment-id}")
//    public ResponseEntity<?> updateComment(@RequestBody @Valid CommentUpdateDto commentUpdateDto){
//        try {
//        CommentResponseDto commentResponseDto = commentService.updateComment(commentUpdateDto);
//        return ResponseEntity.ok().body(commentResponseDto);
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    // 댓글 D
    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/{comment-id}")
    public ResponseEntity<?> deleteComment(@PathVariable("comment-id") Long commentId) {

        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok().body("댓글이 삭제되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



//    @DeleteMapping("/{comment-id}")
//    public ResponseEntity<?> deleteComment(	@RequestParam Long memberId, @RequestParam Long boardId,
//                                                  @PathVariable("comment-id") Long commentId) {
//
//        try {
//            commentService.deleteComment(commentId);
//
//            return ResponseEntity.ok().body("댓글이 삭제되었습니다.");
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//    }
}
