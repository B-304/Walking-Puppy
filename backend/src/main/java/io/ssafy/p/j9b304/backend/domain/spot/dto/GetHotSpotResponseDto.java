package io.ssafy.p.j9b304.backend.domain.spot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class GetHotSpotResponseDto {
    private Long spotId;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @Builder
    public GetHotSpotResponseDto(Long spotId, String name, BigDecimal latitude, BigDecimal longitude) {
        this.spotId = spotId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
