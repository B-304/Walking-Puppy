package io.ssafy.p.j9b304.backend.domain.spot.repository;

import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    @Query("SELECT s FROM Spot s WHERE s.deletedAt IS NULL AND s.state = 1")
    List<Spot> findHotSpotAll();
}
