package io.ssafy.p.j9b304.backend.domain.walk.repository;

import io.ssafy.p.j9b304.backend.domain.user.entity.User;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalkRepository extends JpaRepository<Walk, Long> {

    Optional<Walk> findByWalkIdAndUser(Long walkId, User user);

    List<Walk> findByUserAndState(User walker, char c);
}
