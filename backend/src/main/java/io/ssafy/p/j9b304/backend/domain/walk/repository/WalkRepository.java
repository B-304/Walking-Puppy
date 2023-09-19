package io.ssafy.p.j9b304.backend.domain.walk.repository;

import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkRepository extends JpaRepository<Walk, Long> {
}
