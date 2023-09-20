package io.ssafy.p.j9b304.backend.domain.spot.entity;

import io.ssafy.p.j9b304.backend.domain.spot.dto.GetHotSpotResponseDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.GetResponseDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.ModifyRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "spot")
@SQLDelete(sql = "UPDATE spot SET deleted_at = now() WHERE spot_id = ?")
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id")
    private Long spotId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude", nullable = false)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false)
    private BigDecimal longitude;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ColumnDefault("0")
    @Column(name = "state")
    private char state;

    @ColumnDefault("0")
    @Column(name = "open")
    private char open;

    @Builder
    public Spot(Long spotId, String name, BigDecimal latitude, BigDecimal longitude, LocalDateTime createdAt, LocalDateTime deletedAt, char state, char open) {
        this.spotId = spotId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.state = state;
        this.open = open;
    }

    public void modifySpot(ModifyRequestDto spotModifyRequestDto) {
        if (StringUtils.hasText(spotModifyRequestDto.getName()))
            this.name = spotModifyRequestDto.getName();

        // todo : open 데이터 변화가 있다면 수정하는 메서드
    }

    public GetResponseDto toDto() {
        return GetResponseDto.builder()
                .spotId(spotId)
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .open(open)
                .createdAt(createdAt)
                .build();
    }

    public GetHotSpotResponseDto toHotSpotDto() {
        return GetHotSpotResponseDto.builder()
                .spotId(spotId)
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
