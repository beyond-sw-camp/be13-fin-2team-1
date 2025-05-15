package com.gandalp.gandalp.shift.domain.service;

import com.gandalp.gandalp.auth.model.service.AuthService;
import com.gandalp.gandalp.common.entity.CommonCode;
import com.gandalp.gandalp.common.repository.CommonCodeRepository;
import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.hospital.domain.repository.DepartmentRepository;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.member.domain.repository.MemberRepository;
import com.gandalp.gandalp.schedule.domain.entity.Schedule;
import com.gandalp.gandalp.schedule.domain.repository.ScheduleRepository;
import com.gandalp.gandalp.shift.domain.dto.CommentResponseDto;
import com.gandalp.gandalp.shift.domain.dto.ShiftCreateRequestDto;
import com.gandalp.gandalp.shift.domain.dto.ShiftDetailsResponseDto;
import com.gandalp.gandalp.shift.domain.dto.ShiftResponseDto;
import com.gandalp.gandalp.shift.domain.dto.ShiftUpdateDto;
import com.gandalp.gandalp.shift.domain.entity.Board;
import com.gandalp.gandalp.shift.domain.entity.Comment;
import com.gandalp.gandalp.shift.domain.entity.SearchOption;
import com.gandalp.gandalp.shift.domain.repository.CommentRepository;
import com.gandalp.gandalp.shift.domain.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;

    private final CommonCodeRepository commonCodeRepository;

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final AuthService authService;


//    // 교대 요청 댓글 채택
//    @Override
//    @Transactional
//    public void submitComment(Long commentId) {
//        // 1. 댓글 정보 조회
//        Comment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
//
//        // 2. 해당 댓글이 달린 게시글(Board) 조회
//        Board board = comment.getBoard();
//
//        if (!"REQUESTED".equals(board.getBoardStatus())) {
//            throw new IllegalStateException("이미 처리된 교대 요청입니다.");
//        }
//
//        // 3. 게시글 상태 COMPLETED로 변경
//        board.setBoardStatus("COMPLETED");
//
//        // 4. 일정(Schedule)에서 nurseId 서로 교환
//        // 예시: 게시글의 일정과 댓글 작성자의 일정이 같은 날짜/시간대에 있다고 가정
//        Long boardScheduleId = board.getScheduleId(); // 예시: Board에 scheduleId 필드가 있다고 가정
//        Long commentScheduleId = comment.getScheduleId(); // 예시: Comment에 scheduleId 필드가 있다고 가정
//
//        Schedule boardSchedule = scheduleRepository.findById(boardScheduleId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글의 일정을 찾을 수 없습니다."));
//        Schedule commentSchedule = scheduleRepository.findById(commentScheduleId)
//                .orElseThrow(() -> new IllegalArgumentException("댓글 작성자의 일정을 찾을 수 없습니다."));
//
//        // nurseId 서로 교환
//        Long tempNurseId = boardSchedule.getNurseId();
//        boardSchedule.setNurseId(commentSchedule.getNurseId());
//        commentSchedule.setNurseId(tempNurseId);
//
//        // 5. 저장 (JPA의 경우 @Transactional이면 자동 flush)
//        // 명시적으로 저장하고 싶으면 아래처럼
//        scheduleRepository.save(boardSchedule);
//        scheduleRepository.save(commentSchedule);
//        // board의 상태도 저장
//        shiftRepository.save(board);
//
//        // 필요하다면 댓글에 "채택됨" 표시 등 추가
//        comment.setSelected(true);
//        commentRepository.save(comment);
//    }

    // 공통 코드 변환 메서드
    private ShiftResponseDto toDto(Board board) {
        String label = commonCodeRepository
                .findByCodeGroupAndCodeValue("board_status", board.getBoardStatus())
                .map(CommonCode::getCodeLabel)
                .orElse(null);

        // 라벨을 포함하는 생성자(혹은 setter)로 DTO 생성
        ShiftResponseDto dto = new ShiftResponseDto(board);
        dto.setBoardStatusLabel(label);
        return dto;
    }

    // 교대 요청 글 C
    @Override
    public ShiftResponseDto createShift(ShiftCreateRequestDto shiftCreateRequestDto) {

        Member member = authService.getLoginMember();
        Department department = member.getDepartment();

        // DTO -> entity 로 변환
        // createAt = 입력한 memberid의 회원의 accountid
        Board board = Board.builder()
                .content(shiftCreateRequestDto.getContent())
//                .boardStatus(shiftCreateRequestDto.getBoardStatus())
                .createdAt(LocalDateTime.now())
                .createdBy(member.getAccountId())
                .comments(null)
                .department(department)
                .member(member)
                .build();

        // repository 에 entity 저장
        shiftRepository.save(board);

        // entity -> responseDto 로 변환 후 반환
        return new ShiftResponseDto(board);

    }

    // 교대 요청 글 R

    // 교대 요청 글 검색 조회
    @Override
    public Page<ShiftResponseDto> getSearchingAll(String keyword, SearchOption searchOption, Pageable pageable) {
        // 1. 로그인한 사용자 조회
        Member member = authService.getLoginMember();
        Department department = member.getDepartment();

        // 2. 부서 기준으로 검색
        return shiftRepository.getSearchingAllByDepartment(department, keyword, searchOption, pageable);
    }


    // 교대 요청 글 기본 조회


    @Override
    public Page<ShiftResponseDto> getAll(Pageable pageable) {
        // 1. 로그인 사용자 조회
        Member member = authService.getLoginMember();
        Department department = member.getDepartment();

        // 2. 해당 부서의 게시글 페이징 조회
        Page<Board> boardPage = shiftRepository.findAllByDepartment(department, pageable);

        // 3. Board → ShiftResponseDto 변환 (boardStatusLabel 포함)
        Page<ShiftResponseDto> dtoPage = boardPage.map(this::toDto);

        return dtoPage;
    }

    //    @Override
//    public Page<ShiftResponseDto> getAll( Pageable pageable) {
//        Member member = authService.getLoginMember();
//        Department department = member.getDepartment();
//        Page<Board> boardPage = shiftRepository.findAllByDepartment(department);
//
//        Page<ShiftResponseDto> =
//    }

    // 교대 요청 글 단건 상세 조회
    public ShiftDetailsResponseDto getShiftDetails(Long boardId) {
        Board board = shiftRepository.findByIdWithComments(boardId)
                .orElseThrow(() -> new RuntimeException("해당 글을 찾을 수 없습니다."));

        List<CommentResponseDto> commentDtos = board.getComments().stream()
                .map(comment -> new CommentResponseDto(comment.getId(), comment.getContent(), comment.getCreatedAt()))
                .collect(Collectors.toList());

        return new ShiftDetailsResponseDto(
                board.getId(),
                board.getContent(),
                board.getBoardStatus(),
                commentDtos
        );
    }


    // 교대 요청 글 U
    @Override
    public ShiftResponseDto updateShift(ShiftUpdateDto shiftUpdateDto) {

        // 1. 로그인 사용자 조회
        Member member = authService.getLoginMember();


        Long boardId = shiftUpdateDto.getBoardId();
        Board board = shiftRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        board.update(shiftUpdateDto, member.getAccountId());
        return new ShiftResponseDto(board);
    }

    // 교대 요청 글 D
    @Override
    public void deleteShift(Long boardId) {

        // 게시판, 회원, 부서가 존재하는지 확인

        // 1. 로그인 사용자 조회
        Member member = authService.getLoginMember();
        Department department = member.getDepartment();

        Board board = shiftRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );


        shiftRepository.deleteById(boardId);
    }



}

