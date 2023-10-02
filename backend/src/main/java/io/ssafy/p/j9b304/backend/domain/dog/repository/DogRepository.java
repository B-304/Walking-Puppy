package io.ssafy.p.j9b304.backend.domain.dog.repository;

import io.ssafy.p.j9b304.backend.domain.dog.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DogRepository extends JpaRepository<Dog, Long> {
    Optional<Dog> findByDogId(Long dogId);

}
