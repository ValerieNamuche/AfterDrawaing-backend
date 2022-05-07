package com.afterdrawing.backendapi.core.repository;

import com.afterdrawing.backendapi.core.entity.Wireframe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WireframeRepository extends JpaRepository<Wireframe,Long> {
}
