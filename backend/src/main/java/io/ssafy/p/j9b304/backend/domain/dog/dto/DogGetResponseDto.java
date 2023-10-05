package io.ssafy.p.j9b304.backend.domain.dog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DogGetResponseDto {
    private Long dogId;
    private String name;
    private LocalDateTime createdAt;
    private char dogLevel;
    private int exp;
    private String imagePath;
    private Long dayCount;
    private int levelRange;

    @Builder
    public DogGetResponseDto(Long dogId, String name, LocalDateTime createdAt, char dogLevel, int exp, String imagePath, Long dayCount, int levelRange) {
        this.dogId = dogId;
        this.name = name;
        this.createdAt = createdAt;
        this.dogLevel = dogLevel;
        this.exp = exp;
        this.imagePath = imagePath;
        this.dayCount = dayCount;
        this.levelRange = levelRange;
    }
}
