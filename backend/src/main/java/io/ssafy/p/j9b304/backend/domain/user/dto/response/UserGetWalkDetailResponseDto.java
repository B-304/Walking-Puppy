package io.ssafy.p.j9b304.backend.domain.user.dto.response;

import io.ssafy.p.j9b304.backend.domain.walk.entity.Theme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserGetWalkDetailResponseDto {
    @Schema(description = "산책 식별번호")
    private Long walkld;

    @Schema(description = "소요시간(분)")
    private long durationTime;

    @Schema(description = "걸음수")
    private Integer walkCount;

    @Schema(description = "테마")
    private Theme theme;

    @Schema(description = "출발지 위도")
    private BigDecimal startLatitude;

    @Schema(description = "출발지 경도")
    private BigDecimal startLongitude;

    @Schema(description = "도착지 위도")
    private BigDecimal endLatitude;

    @Schema(description = "도착지 경도")
    private BigDecimal endLongitude;

    @Schema(description = "시작 시간")
    private LocalDateTime startTime;

    @Schema(description = "도착 시간")
    private LocalDateTime endTime;

    @Builder
    public UserGetWalkDetailResponseDto(Long walkld, long durationTime, Integer walkCount, Theme theme, BigDecimal startLatitude, BigDecimal startLongitude, BigDecimal endLatitude, BigDecimal endLongitude, LocalDateTime startTime, LocalDateTime endTime) {
        this.walkld = walkld;
        this.durationTime = durationTime;
        this.walkCount = walkCount;
        this.theme = theme;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

