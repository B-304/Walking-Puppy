package io.ssafy.p.j9b304.backend.domain.walk.entity;

import io.ssafy.p.j9b304.backend.domain.user.dto.response.UserGetWalkDetailResponseDto;
import io.ssafy.p.j9b304.backend.domain.user.entity.User;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkSaveRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Walk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walkId;

    @Setter
    @Column(name = "name", length = 20)
    private String name;

    // '0' : 산책 시작 전, '1' : 산책 완료, '2' : 산책 저장
    @Column(name = "state")
    private Character state;

    // decimal(18,10)
    @Column(name = "start_latitude")
    private double startLatitude;

    @Column(name = "start_longitude")
    private double startLongitude;

    @Column(name = "end_latitude")
    private double endLatitude;

    @Column(name = "end_longitude")
    private double endLongitude;

    @CreatedDate
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Setter
    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Setter
    @Column(name = "estimated_distance")
    private Double estimatedDistance;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "walk_count")
    private Integer walkCount;

    // smallint
    @Column(name = "calorie")
    private Short calorie;

    @Setter
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        this.state = this.state == null ? '0' : this.state;
        this.estimatedDistance = this.estimatedDistance == null ? 0 : this.estimatedDistance;
        this.distance = this.distance == null ? 0 : this.distance;
        this.walkCount = this.walkCount == null ? 0 : this.walkCount;
        this.calorie = this.calorie == null ? 0 : this.calorie;
    }

    // 산책 종료 시 데이터 저장
    public void walkOver(WalkSaveRequestDto walkSaveRequestDto) {
        this.state = '1';
        this.distance = walkSaveRequestDto.getDistance();
        this.walkCount = walkSaveRequestDto.getWalkCount();
        this.calorie = walkSaveRequestDto.getCalorie();
        this.endTime = LocalDateTime.now();
    }

    public void scrap(String name) {
        this.state = '2';
        this.name = name;
    }

    public void removeScrap() {
        this.state = '1';
    }

    public void walkExistPath(Walk walkScrap, Route start, Route end) {
        this.state = '0';
        this.theme = walkScrap.theme;
        Long walkDuration = ChronoUnit.MINUTES.between(walkScrap.getStartTime(), walkScrap.getEndTime());
        this.estimatedTime = walkDuration.intValue();
        this.estimatedDistance = walkScrap.getDistance();
        this.startLatitude = start.getLatitude();
        this.startLongitude = start.getLongitude();
        this.endLatitude = end.getLatitude();
        this.endLongitude = end.getLongitude();
    }

    public UserGetWalkDetailResponseDto toUserGetWalkDetailResponseDto() {
        return UserGetWalkDetailResponseDto.builder()
                .walkld(walkId)
                .durationTime(Duration.between(startTime, endTime).getSeconds() / 60)
                .walkCount(walkCount)
                .theme(theme)
                .startTime(startTime)
                .endTime(endTime)
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
                .endLatitude(endLatitude)
                .endLongitude(endLongitude)
                .build();
    }
}
