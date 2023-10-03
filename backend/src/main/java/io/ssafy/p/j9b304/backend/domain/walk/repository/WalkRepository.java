package io.ssafy.p.j9b304.backend.domain.walk.repository;

import io.ssafy.p.j9b304.backend.domain.user.entity.User;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WalkRepository extends JpaRepository<Walk, Long> {

    Optional<Walk> findByWalkIdAndUser(Long walkId, User user);

    List<Walk> findByUserAndState(User walker, char c);

    @Query("SELECT w FROM Walk w WHERE w.user = ?1  AND w.state = '1' AND DATE(w.startTime) = DATE(now())")
    List<Walk> findByUser(User walker);

    List<Walk> findByStartTimeBetween(LocalDateTime startTime1, LocalDateTime startTime2);
}
