package com.gandalp.gandalp.member.domain.repository;

import com.gandalp.gandalp.member.domain.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NurseRepository extends JpaRepository<Nurse, Long>, NurseRepositoryCustom {

    Optional<Nurse> findByEmail(String email);

    Optional<Nurse> findByPasswordAndEmail(String password, String email);

}
