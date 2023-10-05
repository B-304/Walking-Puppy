package io.ssafy.p.j9b304.backend.domain.walk.dto.request;

import io.ssafy.p.j9b304.backend.domain.walk.entity.Route;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class RouteAddRequestDto {
    @Schema(description = "순서")
    Integer sequence;

    @Schema(description = "위도")
    double latitude;

    @Schema(description = "경도")
    double longitude;

    public Route toEntity(Walk walk, char state) {
        return Route.builder()
                .state(state)
                .sequence(this.sequence)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .walk(walk)
                .build();
    }
}
