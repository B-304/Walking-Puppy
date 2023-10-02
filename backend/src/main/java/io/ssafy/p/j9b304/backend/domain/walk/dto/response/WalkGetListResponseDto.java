package io.ssafy.p.j9b304.backend.domain.walk.dto.response;

import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.temporal.ChronoUnit;


@Getter
@NoArgsConstructor
public class WalkGetListResponseDto {
    @Schema(description = "산책 식별번호")
    Long walkId;

    @Schema(description = "산책로명")
    String name;

    @Schema(description = "소요 시간(분)")
    Integer time;

    @Schema(description = "이동 거리(km)")
    Float distance;

    @Setter
    @Schema(description = "산책로 지도 이미지")
    String imageUrl;

    public WalkGetListResponseDto(Walk walk) {
        this.walkId = walk.getWalkId();
        this.name = walk.getName();
        Long walkDuration = ChronoUnit.MINUTES.between(walk.getStartTime(), walk.getEndTime());
        this.time = walkDuration.intValue();
        this.distance = walk.getDistance();
    }
}
