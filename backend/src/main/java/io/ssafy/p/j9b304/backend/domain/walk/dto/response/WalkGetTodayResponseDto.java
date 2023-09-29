package io.ssafy.p.j9b304.backend.domain.walk.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WalkGetTodayResponseDto {
    @Schema(description = "걸음수")
    private Integer walkCount;

    @Schema(description = "이동거리(km)")
    private Float distance;

    @Schema(description = "칼로리")
    private Short calorie;

    @Builder
    public WalkGetTodayResponseDto(Integer walkCount, Float distance, Short calorie) {
        this.walkCount = walkCount;
        this.distance = distance;
        this.calorie = calorie;
    }
}
