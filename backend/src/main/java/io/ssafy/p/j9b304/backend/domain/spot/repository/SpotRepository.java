package io.ssafy.p.j9b304.backend.domain.spot.repository;

import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {
}
