package io.ssafy.p.j9b304.backend.global.repository;

import io.ssafy.p.j9b304.backend.global.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.deletedAt IS NULL AND f.fileId =?1")
    Optional<File> findById(Long fileId);
}
