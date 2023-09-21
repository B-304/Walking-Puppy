package io.ssafy.p.j9b304.backend.domain.walk.entity;

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
    @Column(name = "start_latitude", precision = 18, scale = 10)
    private BigDecimal startLatitude;

    @Column(name = "start_longitude", precision = 18, scale = 10)
    private BigDecimal startLongitude;

    @Column(name = "end_latitude", precision = 18, scale = 10)
    private BigDecimal endLatitude;

    @Column(name = "end_longitude", precision = 18, scale = 10)
    private BigDecimal endLongitude;

    @CreatedDate
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Column(name = "estimated_distance")
    private Float estimatedDistance;

    @Column(name = "distance")
    private Float distance;

    @Column(name = "walk_count")
    private Integer walkCount;

    // smallint
    @Column(name = "calorie")
    private Short calorie;

    @Column(name = "image_id")
    private Integer imageId;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

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

    public void scrap() {
        this.state = '2';
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
}
