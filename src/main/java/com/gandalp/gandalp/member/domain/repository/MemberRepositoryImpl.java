package com.gandalp.gandalp.member.domain.repository;

import com.gandalp.gandalp.member.domain.dto.MemberResponseDto;
import com.gandalp.gandalp.member.domain.entity.MemberSearchOption;
import com.gandalp.gandalp.member.domain.entity.Type;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gandalp.gandalp.hospital.domain.entity.QDepartment.department;
import static com.gandalp.gandalp.hospital.domain.entity.QHospital.hospital;
import static com.gandalp.gandalp.member.domain.entity.QMember.member;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    // 멤버 검색 조건
    private BooleanExpression searchOptions(String keyword, MemberSearchOption option) {

        // 옵션 값이 없으면 전체 조회(최신 순)
        // 검색어가 있으면 검색 옵션도 있어야 함
        if(option == null || keyword == null || keyword.isBlank()) {

            return null;
        }

        if (option == MemberSearchOption.HOSPITAL) {

            return member.hospital.name.containsIgnoreCase(keyword);

        } else if (option == MemberSearchOption.DEPARTMENT) {

            return member.department.name.containsIgnoreCase(keyword);

        } else if (option == MemberSearchOption.ACCOUNT_ID) {

            return member.accountId.contains(keyword);
        }

        // 기본 전체 조회
        return null;
    }




    @Override
    public Page<MemberResponseDto> searchMembers(String keyword, Type type, MemberSearchOption option, Pageable pageable){

        // 검색 조건
        BooleanExpression typePredicate = (type != null)
                ? member.type.eq(type)
                : null;

        BooleanExpression searchPredicate = searchOptions(keyword, option);

        List<MemberResponseDto> content = queryFactory

                .select(Projections.constructor(MemberResponseDto.class,
                        member.id,
                        hospital.name,
                        department.name,
                        member.accountId,
                        member.type
                        ))
                .from(member)
                .leftJoin(member.hospital, hospital)
                .leftJoin(member.department, department)
                .where(
                        typePredicate,
                        searchPredicate
                )
                .orderBy(member.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount =queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.hospital, hospital)
                .leftJoin(member.department, department)
                .where(
                        typePredicate,
                        searchPredicate
                );

        return PageableExecutionUtils.getPage(content,pageable, totalCount::fetchOne);

    }

}
