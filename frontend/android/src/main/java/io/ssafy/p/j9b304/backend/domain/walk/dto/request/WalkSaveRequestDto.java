package io.ssafy.p.j9b304.backend.domain.walk.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WalkSaveRequestDto {
    @Schema(description = "산책 식별번호")
    Long walkId;

    @Schema(description = "실제 이동거리")
    Double distance;

    @Schema(description = "걸음 수")
    Integer walkCount;

    @Schema(description = "칼로리(kcal)")
    Short calorie;

    @Schema(description = "간식 획득 카운트")
    Integer itemCount;

    @Schema(description = "사용자 산책 경로 리스트")
    List<RouteAddRequestDto> route;

}
