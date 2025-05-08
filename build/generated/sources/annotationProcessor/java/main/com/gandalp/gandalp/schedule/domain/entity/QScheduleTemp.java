package com.gandalp.gandalp.schedule.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScheduleTemp is a Querydsl query type for ScheduleTemp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScheduleTemp extends EntityPathBase<ScheduleTemp> {

    private static final long serialVersionUID = 1327610436L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScheduleTemp scheduleTemp = new QScheduleTemp("scheduleTemp");

    public final com.gandalp.gandalp.common.entity.QBaseEntity _super = new com.gandalp.gandalp.common.entity.QBaseEntity(this);

    public final EnumPath<TempCategory> category = createEnum("category", TempCategory.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.gandalp.gandalp.member.domain.entity.QNurse nurse;

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QScheduleTemp(String variable) {
        this(ScheduleTemp.class, forVariable(variable), INITS);
    }

    public QScheduleTemp(Path<? extends ScheduleTemp> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScheduleTemp(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScheduleTemp(PathMetadata metadata, PathInits inits) {
        this(ScheduleTemp.class, metadata, inits);
    }

    public QScheduleTemp(Class<? extends ScheduleTemp> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.nurse = inits.isInitialized("nurse") ? new com.gandalp.gandalp.member.domain.entity.QNurse(forProperty("nurse"), inits.get("nurse")) : null;
    }

}

