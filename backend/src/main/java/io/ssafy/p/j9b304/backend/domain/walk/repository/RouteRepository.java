package io.ssafy.p.j9b304.backend.domain.walk.repository;

import io.ssafy.p.j9b304.backend.domain.walk.entity.Route;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findByWalkAndState(Walk walk, char state);


}
