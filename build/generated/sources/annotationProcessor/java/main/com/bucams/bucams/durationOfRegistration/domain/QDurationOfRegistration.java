package com.bucams.bucams.durationOfRegistration.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDurationOfRegistration is a Querydsl query type for DurationOfRegistration
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDurationOfRegistration extends EntityPathBase<DurationOfRegistration> {

    private static final long serialVersionUID = -1165824623L;

    public static final QDurationOfRegistration durationOfRegistration = new QDurationOfRegistration("durationOfRegistration");

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public QDurationOfRegistration(String variable) {
        super(DurationOfRegistration.class, forVariable(variable));
    }

    public QDurationOfRegistration(Path<? extends DurationOfRegistration> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDurationOfRegistration(PathMetadata metadata) {
        super(DurationOfRegistration.class, metadata);
    }

}

