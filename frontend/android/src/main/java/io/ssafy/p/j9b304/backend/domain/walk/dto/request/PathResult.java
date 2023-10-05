package io.ssafy.p.j9b304.backend.domain.walk.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class PathResult {
    private DijkstraResult destination;
    private List<Point> path;

    public PathResult(DijkstraResult destination, List<Point> path) {
        this.destination = destination;
        this.path = path;
    }

}
