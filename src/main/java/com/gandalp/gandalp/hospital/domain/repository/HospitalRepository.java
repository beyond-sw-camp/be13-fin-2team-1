package com.gandalp.gandalp.hospital.domain.repository;

import com.gandalp.gandalp.hospital.domain.entity.Department;
import com.gandalp.gandalp.hospital.domain.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital,Long>, HospitalRepositoryCustom {

    Optional<Hospital> findByName(String name);

    // 위도, 경도가 없는 병원만 찾음
    List<Hospital> findByLatitudeIsNullOrLongitudeIsNull();


}
