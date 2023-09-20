package io.ssafy.p.j9b304.backend.domain.walk.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    // '0' : 추천 산책 경로, '1' : 사용자 산책 경로
    @Column(name = "state")
    private char state;

    @Column(name = "sequence")
    private Integer sequence;

    // decimal(18,10)
    @Column(name = "latitude", precision = 18, scale = 10)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 18, scale = 10)
    private BigDecimal longitude;

    @ManyToOne
    @JoinColumn(name = "walk_id")
    private Walk walk;
}
