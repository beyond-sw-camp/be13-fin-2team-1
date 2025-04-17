package com.bucams.bucams.registration.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRegistration is a Querydsl query type for Registration
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRegistration extends EntityPathBase<Registration> {

    private static final long serialVersionUID = -749138629L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRegistration registration = new QRegistration("registration");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.bucams.bucams.lecture.domain.entity.QLecture lecture;

    public final com.bucams.bucams.member.domain.QMember member;

    public final DateTimePath<java.time.LocalDateTime> registeredAt = createDateTime("registeredAt", java.time.LocalDateTime.class);

    public QRegistration(String variable) {
        this(Registration.class, forVariable(variable), INITS);
    }

    public QRegistration(Path<? extends Registration> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRegistration(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRegistration(PathMetadata metadata, PathInits inits) {
        this(Registration.class, metadata, inits);
    }

    public QRegistration(Class<? extends Registration> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lecture = inits.isInitialized("lecture") ? new com.bucams.bucams.lecture.domain.entity.QLecture(forProperty("lecture"), inits.get("lecture")) : null;
        this.member = inits.isInitialized("member") ? new com.bucams.bucams.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

