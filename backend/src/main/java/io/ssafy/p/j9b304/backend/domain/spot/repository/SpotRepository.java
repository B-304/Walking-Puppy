package io.ssafy.p.j9b304.backend.domain.spot.repository;

import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    @Query("SELECT s FROM Spot s WHERE s.deletedAt IS NULL AND s.state = 1")
    List<Spot> findHotSpotAll();

    @Query("SELECT s FROM Spot s WHERE s.deletedAt IS NULL AND s.state = 1 AND s.spotId =?1")
    Spot findHotSpotByID(Long spotId);

    @Query("SELECT s FROM Spot s WHERE s.deletedAt IS NULL AND s.state = 0")
    List<Spot> findSpotAll();

    @Query("SELECT s FROM Spot s WHERE s.deletedAt IS NULL AND s.state = 0 AND s.spotId =?1")
    Optional<Spot> findSpotById(Long spotId);
}
