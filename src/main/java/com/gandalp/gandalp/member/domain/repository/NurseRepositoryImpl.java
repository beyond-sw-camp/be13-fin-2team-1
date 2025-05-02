package com.gandalp.gandalp.member.domain.repository;

import com.gandalp.gandalp.member.domain.dto.NurseResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gandalp.gandalp.member.domain.entity.QNurse.nurse;

@Repository
public class NurseRepositoryImpl implements NurseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NurseRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<NurseResponseDto> getAll(String keyword, Pageable pageable, Long departmentId ) {

        BooleanBuilder search = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {
            search.and(nurse.name.contains(keyword));
        }

        if (departmentId != null) {
            search.and(nurse.department.id.eq(departmentId));
        }

        List<NurseResponseDto> nurseList = queryFactory
                .select(Projections.constructor(NurseResponseDto.class,
                        nurse.name,
                        nurse.email
                ))
                .from(nurse)
                .where(search)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = queryFactory
                .select(nurse.count())
                .from(nurse)
                .where(search);

        return PageableExecutionUtils.getPage(nurseList, pageable, totalCount::fetchOne);
    }



}
