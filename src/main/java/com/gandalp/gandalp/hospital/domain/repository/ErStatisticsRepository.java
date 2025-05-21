package com.gandalp.gandalp.hospital.domain.repository;

import com.gandalp.gandalp.hospital.domain.entity.ErStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErStatisticsRepository extends JpaRepository<ErStatistics, Long>, ErStatisticsRepositoryCustom {

}
