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

    List<Walk> findByUserAndState(User walker, Character c);

    @Query("SELECT w FROM Walk w WHERE w.user = :walker AND (w.state = '1' OR w.state = '2') AND DATE(w.startTime) = CURRENT_DATE")
    List<Walk> findByUser(User walker);


//    List<Walk> findByStartTimeBetween(LocalDateTime startTime1, LocalDateTime startTime2);

    List<Walk> findByStartTimeBetweenAndStateAndUser(LocalDateTime localDateTime, LocalDateTime localDateTime1, Character state, User user);

}
