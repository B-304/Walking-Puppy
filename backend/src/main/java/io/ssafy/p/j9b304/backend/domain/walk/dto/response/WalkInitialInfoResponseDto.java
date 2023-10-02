package io.ssafy.p.j9b304.backend.domain.walk.dto.response;

import io.ssafy.p.j9b304.backend.domain.spot.dto.GetResponseDto;
import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Route;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class WalkInitialInfoResponseDto {
    @Schema(description = "산책 식별번호")
    Long walkId;

    @Schema(description = "예상 소요 시간(분)")
    Integer estimatedTime;

    @Schema(description = "예상 이동 거리(km)")
    Float estimatedDistance;

    @Schema(description = "테마명")
    String themeName;

    @Schema(description = "출발지 위도")
    double startLatitude;

    @Schema(description = "출발지 경도")
    double startLongitude;

    @Schema(description = "도착지 위도")
    double endLatitude;

    @Schema(description = "도착지 경도")
    double endLongitude;

    @Schema(description = "산책 경로")
    List<RouteGetResponseDto> routeList;

    @Schema(description = "스팟 리스트")
    List<GetResponseDto> spotList;

    @Schema(description = "간식 스팟 리스트")
    List<GetResponseDto> itemSpotList;

    @Builder
    public WalkInitialInfoResponseDto(Walk walk, List<Route> routeList, List<Spot> spotList, List<Spot> itemSpotList) {
        this.walkId = walk.getWalkId();
        this.estimatedTime = walk.getEstimatedTime();
        this.estimatedDistance = walk.getEstimatedDistance();
        this.themeName = walk.getTheme().getName();
        this.startLatitude = walk.getStartLatitude();
        this.startLongitude = walk.getStartLongitude();
        this.endLatitude = walk.getEndLatitude();
        this.endLongitude = walk.getEndLongitude();
        this.routeList = routeList.stream().map(r -> new RouteGetResponseDto(r)).collect(Collectors.toList());
        this.spotList = spotList.stream().map(Spot::toSpotDto).collect(Collectors.toList());
        this.itemSpotList = itemSpotList.stream().map(Spot::toSpotDto).collect(Collectors.toList());
    }
}
