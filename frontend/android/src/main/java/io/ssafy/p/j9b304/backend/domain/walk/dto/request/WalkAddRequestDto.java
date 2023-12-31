package io.ssafy.p.j9b304.backend.domain.walk.dto.request;

import io.ssafy.p.j9b304.backend.domain.walk.entity.Theme;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class WalkAddRequestDto {
    @Schema(description = "출발지 위도")
    double startLatitude;

    @Schema(description = "출발지 경도")
    double startLongitude;

    @Schema(description = "도착지 위도")
    double endLatitude;

    @Schema(description = "도착지 경도")
    double endLongitude;

    @Schema(description = "스팟 리스트")
    List<Long> spotList;

    @Schema(description = "테마 식별번호")
    Long themeId;

    @Schema(description = "예상 소요시간")
    Integer estimatedTime;

//    @Builder
//    public WalkAddRequestDto(BigDecimal startLatitude, BigDecimal startLongitude, BigDecimal endLatitude, BigDecimal endLongitude, Long themeId, Integer estimatedTime) {
//        this.startLatitude = startLatitude;
//        this.startLongitude = startLongitude;
//        this.endLatitude = endLatitude;
//        this.endLongitude = endLongitude;
//        this.themeId = themeId;
//        this.estimatedTime = estimatedTime;
//    }

    public Walk toEntity(Theme theme) {
        return Walk.builder()
                .state('0')
                .startLatitude(this.startLatitude)
                .startLongitude(this.startLongitude)
                .endLatitude(this.endLatitude)
                .endLongitude(this.endLongitude)
                .theme(theme)
                .estimatedTime(this.estimatedTime)
                .build();
    }

    // todo: checkValidation
}
