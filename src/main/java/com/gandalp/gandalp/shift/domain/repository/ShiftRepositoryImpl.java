package com.gandalp.gandalp.shift.domain.repository;

import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.member.domain.repository.MemberRepository;
import com.gandalp.gandalp.shift.domain.dto.ShiftResponseDto;
import com.gandalp.gandalp.shift.domain.entity.SearchOption;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gandalp.gandalp.shift.domain.entity.QBoard.board;
import static com.gandalp.gandalp.common.entity.QCommonCode.commonCode;


@Repository
public class ShiftRepositoryImpl implements ShiftRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ShiftRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


@Override
public Page<ShiftResponseDto> getAll(Pageable pageable) {
    BooleanBuilder builder = new BooleanBuilder();

    List<ShiftResponseDto> content = queryFactory
            .select(Projections.constructor(
                    ShiftResponseDto.class,
                    board.id,
                    board.content,
                    board.boardStatus,
                    commonCode.codeLabel,
                    board.createdAt
            ))
            .from(board)
            .leftJoin(commonCode)
            .on(commonCode.codeGroup.eq("board_status")
                    .and(commonCode.codeValue.eq(board.boardStatus)))
            .where(builder)
            .orderBy(board.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    long count = queryFactory
            .select(board.count())
            .from(board)
            .where(builder)
            .fetchOne();

    return new PageImpl<>(content, pageable, count);
}


    @Override
    public Page<ShiftResponseDto> getSearchingAllByDepartment(Department department, String keyword, SearchOption searchOption, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(board.department.eq(department));

        if (keyword != null && !keyword.isBlank() && searchOption == SearchOption.CONTENT) {
            builder.and(board.content.containsIgnoreCase(keyword));
        }

        List<ShiftResponseDto> content = queryFactory
                .select(Projections.constructor(
                        ShiftResponseDto.class,
                        board.id,
                        board.content,
                        board.boardStatus,
                        commonCode.codeLabel,
                        board.createdAt
                ))
                .from(board)
                .leftJoin(commonCode)
                .on(commonCode.codeGroup.eq("board_status")
                        .and(commonCode.codeValue.eq(board.boardStatus)))
                .where(builder)
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = queryFactory
                .select(board.count())
                .from(board)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }




}
