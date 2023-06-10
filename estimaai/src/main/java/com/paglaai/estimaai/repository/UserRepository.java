package com.paglaai.estimaai.repository;

import com.paglaai.estimaai.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  UserEntity findByEmail(String email);
}
