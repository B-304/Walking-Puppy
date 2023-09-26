package io.ssafy.p.j9b304.backend.domain.walk.repository;

import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.ssafy.p.j9b304.backend.domain.walk.entity.WalkSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkSpotRepository extends JpaRepository<WalkSpot, Long> {
    List<WalkSpot> findByWalk(Walk walkScrap);
}
