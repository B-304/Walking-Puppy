package io.ssafy.p.j9b304.backend.domain.walk.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DijkstraResult {
    private Point point;
    private double totalWeight;
    private double totalDistance;
    private DijkstraResult previous;

}
