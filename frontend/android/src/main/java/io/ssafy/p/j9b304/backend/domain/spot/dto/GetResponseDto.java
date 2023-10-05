package io.ssafy.p.j9b304.backend.domain.spot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetResponseDto {
    private Long spotId;
    private String name;
    @Column(precision = 18, scale = 10)
    private double latitude;
    @Column(precision = 18, scale = 10)
    private double longitude;
    private LocalDateTime createdAt;
    private char open;

    @Builder
    public GetResponseDto(Long spotId, String name, double latitude, double longitude, LocalDateTime createdAt, char open) {
        this.spotId = spotId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.open = open;
    }
}
