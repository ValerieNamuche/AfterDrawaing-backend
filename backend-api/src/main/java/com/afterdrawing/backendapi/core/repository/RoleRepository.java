package com.afterdrawing.backendapi.core.repository;

import com.afterdrawing.backendapi.core.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Authority, String> {
}
