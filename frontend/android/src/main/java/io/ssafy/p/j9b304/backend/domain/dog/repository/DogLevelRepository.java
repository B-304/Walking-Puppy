package io.ssafy.p.j9b304.backend.domain.dog.repository;

import io.ssafy.p.j9b304.backend.domain.dog.entity.DogLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogLevelRepository extends JpaRepository<DogLevel, Long> {
    DogLevel findByDogLevelId(Long dogLevelId);
}
