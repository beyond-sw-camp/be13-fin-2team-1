package com.gandalp.gandalp.notice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.notice.entity.Category;
import com.gandalp.gandalp.notice.entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {


	List<Notice> findAllByCreatedAtBetweenAndDepartment(LocalDateTime start, LocalDateTime end, Department department);

	List<Notice> findAllByCategoryAndCreatedAtBefore(Category category, LocalDateTime limit);
}
