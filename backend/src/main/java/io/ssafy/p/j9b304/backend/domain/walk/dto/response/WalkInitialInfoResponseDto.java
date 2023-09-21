package io.ssafy.p.j9b304.backend.domain.walk.dto.response;

import io.ssafy.p.j9b304.backend.domain.walk.entity.Route;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Schema(description = "출발지 주소")
    String startAddress;

    @Schema(description = "도착지 주소")
    String endAddress;

    @Schema(description = "산책 경로")
    List<RouteGetResponseDto> routeList;

//    @Schema(description = "스팟 리스트")
//    todo 스팟 리스트 추가

    public WalkInitialInfoResponseDto(Walk walk, List<Route> routeList) {
        this.walkId = walk.getWalkId();
        this.estimatedTime = walk.getEstimatedTime();
        this.estimatedDistance = walk.getEstimatedDistance();
        this.themeName = walk.getTheme().getName();
        // todo 위도, 경도값을 주소로 바꾸기
        this.startAddress = "출발지 주소";
        this.endAddress = "도착지 주소";
        this.routeList = routeList.stream().map(r -> new RouteGetResponseDto(r)).collect(Collectors.toList());
    }
}
