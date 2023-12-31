package io.ssafy.p.j9b304.backend.domain.walk.dto.response;

import io.ssafy.p.j9b304.backend.domain.spot.dto.GetResponseDto;
import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Route;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.ssafy.p.j9b304.backend.global.entity.File;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class WalkGetDetailResponseDto {
    @Schema(description = "산책 식별번호")
    Long walkId;

    @Schema(description = "산책로명")
    String name;

    @Schema(description = "소요 시간(분)")
    Integer time;

    @Schema(description = "이동 거리(km)")
    Double distance;

    @Schema(description = "걸음 수")
    Integer walkCount;

    @Schema(description = "칼로리")
    Short calorie;

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

    @Schema(description = "출발 시간")
    String startTime;

    @Schema(description = "도착 시간")
    String endTime;

    @Schema(description = "산책로 지도 이미지")
    String imageUrl;

    @Schema(description = "예상 소요 시간")
    Integer estimatedTime;

    @Schema(description = "예상 이동 거리")
    Double estimatedDistance;

    @Builder
    public WalkGetDetailResponseDto(Walk walk, List<Route> routeList, List<Spot> spotList, File file) {
        this.walkId = walk.getWalkId();
        this.name = walk.getName();
        long walkDuration = ChronoUnit.MINUTES.between(walk.getStartTime(), walk.getEndTime());
        this.time = Long.valueOf(walkDuration).intValue();
        this.distance = walk.getDistance();
        this.walkCount = walk.getWalkCount();
        this.calorie = walk.getCalorie();
        this.themeName = walk.getTheme().getName();
        this.startLatitude = walk.getStartLatitude();
        this.startLongitude = walk.getStartLongitude();
        this.endLatitude = walk.getEndLatitude();
        this.endLongitude = walk.getEndLongitude();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.startTime = walk.getStartTime().format(formatter);
        this.endTime = walk.getEndTime().format(formatter);
        this.estimatedTime = walk.getEstimatedTime();
        this.estimatedDistance = walk.getEstimatedDistance();
        
        this.routeList = routeList.stream().map(r -> new RouteGetResponseDto(r)).collect(Collectors.toList());
        this.spotList = spotList.stream().map(Spot::toSpotDto).collect(Collectors.toList());
        if (file != null)
            this.imageUrl = file.getUrl();
    }
}
