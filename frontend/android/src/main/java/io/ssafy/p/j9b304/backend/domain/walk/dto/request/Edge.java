package io.ssafy.p.j9b304.backend.domain.walk.dto.request;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;


@Getter
@ToString
public class Edge {
    public Point target;
    public double weight;
    public double distance;

    public Edge(Point target, double weight, double distance) {
        this.target = target;
        this.weight = weight;
        this.distance = distance;
    }
}