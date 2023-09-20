package io.ssafy.p.j9b304.backend.domain.walk.dto.response;

import io.ssafy.p.j9b304.backend.domain.walk.entity.Route;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class RouteGetResponseDto {
    @Schema(description = "순서")
    Integer sequence;

    @Schema(description = "위도")
    BigDecimal latitude;

    @Schema(description = "경도")
    BigDecimal longitude;

    public RouteGetResponseDto(Route route) {
        this.sequence = route.getSequence();
        this.latitude = route.getLatitude();
        this.longitude = route.getLongitude();
    }

}
