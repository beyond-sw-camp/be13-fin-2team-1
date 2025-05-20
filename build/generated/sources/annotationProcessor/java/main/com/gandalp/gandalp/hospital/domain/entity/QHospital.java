package com.gandalp.gandalp.hospital.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHospital is a Querydsl query type for Hospital
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHospital extends EntityPathBase<Hospital> {

    private static final long serialVersionUID = -1372765456L;

    public static final QHospital hospital = new QHospital("hospital");

    public final com.gandalp.gandalp.common.entity.QBaseEntity _super = new com.gandalp.gandalp.common.entity.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final NumberPath<Integer> availableErCount = createNumber("availableErCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath name = createString("name");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Integer> totalErCount = createNumber("totalErCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QHospital(String variable) {
        super(Hospital.class, forVariable(variable));
    }

    public QHospital(Path<? extends Hospital> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHospital(PathMetadata metadata) {
        super(Hospital.class, metadata);
    }

}

