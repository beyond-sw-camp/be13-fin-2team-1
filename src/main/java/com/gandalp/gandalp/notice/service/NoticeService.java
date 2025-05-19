package com.gandalp.gandalp.notice.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gandalp.gandalp.auth.model.service.AuthService;
import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.notice.dto.NoticeDto;
import com.gandalp.gandalp.notice.dto.NoticeCreateResponseDto;
import com.gandalp.gandalp.notice.dto.NoticeResponseDto;
import com.gandalp.gandalp.notice.entity.Category;
import com.gandalp.gandalp.notice.entity.Notice;
import com.gandalp.gandalp.notice.repository.NoticeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

	private final AuthService authService;
	private final NoticeRepository noticeRepository;


	@Transactional
	public NoticeCreateResponseDto createNoticeByHead(NoticeDto createDto){

		Member loginMember = authService.getLoginMember();

		Notice notice = Notice.builder()
			.category(Category.URGENT)
			.content(createDto.getContent())
			.department(loginMember.getDepartment())
			.build();


		noticeRepository.save(notice);

		NoticeCreateResponseDto noticeDto = NoticeCreateResponseDto.builder()
			.id(notice.getId())
			.category(notice.getCategory().toString())
			.content(notice.getContent())
			.departmentName(notice.getDepartment().getName())
			.build();

		return noticeDto;
	}

	// 공지사항 삭제
	@Transactional
	public void deleteNotice(Long noticeId){

		Notice notice = noticeRepository.findById(noticeId).orElseThrow(
			() -> new EntityNotFoundException("해당 공지사항은 존재하지 않습니다.")
		);

		noticeRepository.delete(notice);
	}



	public List<NoticeResponseDto> getNoticeList(){

		// 1. 로그인 했는지 검증
		Member loginMember = authService.getLoginMember();

		// 2. 로그인한 유저의 과 정보 가져오기
		Department department = loginMember.getDepartment();

			// 3. 오늘 날짜 정보 가져오기
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start = now.minusDays(3);

		// 4. (오늘 날짜 - 3)일치 공지사항을 가져오기
		List<Notice> allList = noticeRepository.findAllByCreatedAtBetweenAndDepartment(start, now,department);


		return allList.stream()
			.sorted(Comparator.comparing(Notice::getCategory))
			.map(notice -> new NoticeResponseDto(notice.getContent()) )
			.toList();

	}


	// 생성된지 3일된 일반 공지사항은 자동삭제된다.
	@Transactional
	@Scheduled(cron = "0 0 0 * * *") // 매일 자정에 자동 실행
	public void autoDelete(){

		LocalDateTime limit = LocalDateTime.now().minusDays(3);

		List<Notice> deleteList = noticeRepository.findAllByCategoryAndCreatedAtBefore(
			Category.GENERAL, limit);

		noticeRepository.deleteAll(deleteList);

	}


}
