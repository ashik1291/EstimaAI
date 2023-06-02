package com.paglaai.estimaai.repository;

import com.paglaai.estimaai.repository.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
