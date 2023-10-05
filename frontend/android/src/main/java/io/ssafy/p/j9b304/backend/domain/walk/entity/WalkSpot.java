package io.ssafy.p.j9b304.backend.domain.walk.entity;

import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalkSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walkSpotId;

    @ManyToOne
    @JoinColumn(name = "walk_id")
    private Walk walk;

    @ManyToOne
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @Builder
    public WalkSpot(Walk walk, Spot spot) {
        this.walk = walk;
        this.spot = spot;
    }
}
