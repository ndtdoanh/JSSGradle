package com.ndtdoanh.JSSGradle.repository;

import com.ndtdoanh.JSSGradle.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
  User findByEmail(String email);

  boolean existsByEmail(String email);

  User findByRefreshTokenAndEmail(String refreshToken, String email);
}
