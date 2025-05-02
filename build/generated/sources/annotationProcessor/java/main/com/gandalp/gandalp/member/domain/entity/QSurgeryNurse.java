package com.gandalp.gandalp.member.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSurgeryNurse is a Querydsl query type for SurgeryNurse
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSurgeryNurse extends EntityPathBase<SurgeryNurse> {

    private static final long serialVersionUID = -2016270402L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSurgeryNurse surgeryNurse = new QSurgeryNurse("surgeryNurse");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QNurse nurse;

    public final com.gandalp.gandalp.schedule.domain.entity.QSurgerySchedule surgerySchedule;

    public QSurgeryNurse(String variable) {
        this(SurgeryNurse.class, forVariable(variable), INITS);
    }

    public QSurgeryNurse(Path<? extends SurgeryNurse> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSurgeryNurse(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSurgeryNurse(PathMetadata metadata, PathInits inits) {
        this(SurgeryNurse.class, metadata, inits);
    }

    public QSurgeryNurse(Class<? extends SurgeryNurse> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.nurse = inits.isInitialized("nurse") ? new QNurse(forProperty("nurse"), inits.get("nurse")) : null;
        this.surgerySchedule = inits.isInitialized("surgerySchedule") ? new com.gandalp.gandalp.schedule.domain.entity.QSurgerySchedule(forProperty("surgerySchedule"), inits.get("surgerySchedule")) : null;
    }

}

