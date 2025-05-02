package com.gandalp.gandalp.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditableEntity is a Querydsl query type for AuditableEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAuditableEntity extends EntityPathBase<AuditableEntity> {

    private static final long serialVersionUID = -149861039L;

    public static final QAuditableEntity auditableEntity = new QAuditableEntity("auditableEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath createdBy = createString("createdBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath updatedBy = createString("updatedBy");

    public QAuditableEntity(String variable) {
        super(AuditableEntity.class, forVariable(variable));
    }

    public QAuditableEntity(Path<? extends AuditableEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuditableEntity(PathMetadata metadata) {
        super(AuditableEntity.class, metadata);
    }

}

