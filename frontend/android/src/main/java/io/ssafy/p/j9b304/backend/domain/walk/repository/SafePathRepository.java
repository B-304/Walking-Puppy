package io.ssafy.p.j9b304.backend.domain.walk.repository;

import io.ssafy.p.j9b304.backend.domain.walk.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SafePathRepository extends JpaRepository<Path,Long> {

//    @Query("SELECT sp FROM SafePath sp " +
//            "WHERE (6371 * acos(cos(radians(:nowY)) * cos(radians(sp.startY)) * " +
//            "cos(radians(sp.startX) - radians(:nowX)) + sin(radians(:nowY)) * " +
//            "sin(radians(sp.startY)))) <= :radius")
//    List<SafePath> findSafePathsInRadius(@Param("nowX") double nowX,
//                                         @Param("nowY") double nowY,
//                                         @Param("radius") double radius);

    @Query(value = "SELECT s FROM Path s WHERE " +
            "s.pathType = :type AND " +
            "(6371 * acos(cos(radians(:nowY)) * cos(radians(s.startY)) * cos(radians(s.startX) - radians(:nowX)) + sin(radians(:nowY)) * sin(radians(s.startY))) <= :r) AND " +
            "(6371 * acos(cos(radians(:nowY)) * cos(radians(s.endY)) * cos(radians(s.endX) - radians(:nowX)) + sin(radians(:nowY)) * sin(radians(s.endY))) <= :r)")
    List<Path> findSafePathsInRadius(@Param("nowX") double nowX,
                                     @Param("nowY") double nowY,
                                     @Param("r") double r,
                                     @Param("type") Integer type);



}
