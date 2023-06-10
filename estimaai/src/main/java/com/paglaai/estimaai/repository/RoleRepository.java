package com.paglaai.estimaai.repository;

import com.paglaai.estimaai.repository.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

  RoleEntity findByName(String name);
}
