package io.ssafy.p.j9b304.backend.domain.walk.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Point {
    private double x;
    private double y;

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Point point = (Point) o;
//        return x.compareTo(point.x) == 0 && y.compareTo(point.y) == 0;
//    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
