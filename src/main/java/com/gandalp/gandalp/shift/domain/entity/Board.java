package com.gandalp.gandalp.shift.domain.entity;

import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.member.domain.entity.Member;
import com.gandalp.gandalp.shift.domain.dto.ShiftUpdateDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board-id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member-id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department-id")
	private Department department;

	@Column(nullable = false, length = 200)
	private String content;

	@Builder.Default
	@Column(nullable = false, name = "board_status")
	private String boardStatus = "Waiting";


	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(length = 30)
	private String createdBy;

	@Column
	private LocalDateTime updatedAt;

	@Column(length = 30)
	private String updatedBy;

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();


	@Builder
	public Board(ShiftUpdateDto shiftUpdateDto, Member member, Department department) {
		this.content = shiftUpdateDto.getContent();
		this.member = member;
		this.department = department;

	}


	public void update(ShiftUpdateDto shiftUpdateDto, String updatedByAccountId) {
		this.content = shiftUpdateDto.getContent();
		this.updatedAt = LocalDateTime.now();
		this.updatedBy = updatedByAccountId;

	}

}
