package com.gandalp.gandalp.common.repository;

import com.gandalp.gandalp.common.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode,Long> {
    @Query("SELECT c.codeLabel FROM CommonCode c WHERE c.codeGroup = :codeGroup AND c.codeValue = :codeValue")
    Optional<String> findCodeLabelByCodeGroupAndCodeValue(String codeGroup, String codeValue);

    Optional<CommonCode> findByCodeGroupAndCodeValue(String codeGroup, String codeValue);

}
