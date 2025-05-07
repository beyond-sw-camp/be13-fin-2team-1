package com.gandalp.gandalp.common.repository;

import com.gandalp.gandalp.common.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode,Long> {
    Optional<String> findCodeLabelByCodeGroupAndCodeValue(String codeGroup, String codeValue);
}
