package com.gandalp.gandalp.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommonCode is a Querydsl query type for CommonCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommonCode extends EntityPathBase<CommonCode> {

    private static final long serialVersionUID = -1978884737L;

    public static final QCommonCode commonCode = new QCommonCode("commonCode");

    public final StringPath codeGroup = createString("codeGroup");

    public final StringPath codeLabel = createString("codeLabel");

    public final StringPath codeValue = createString("codeValue");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final ComparablePath<Character> useYn = createComparable("useYn", Character.class);

    public QCommonCode(String variable) {
        super(CommonCode.class, forVariable(variable));
    }

    public QCommonCode(Path<? extends CommonCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommonCode(PathMetadata metadata) {
        super(CommonCode.class, metadata);
    }

}

