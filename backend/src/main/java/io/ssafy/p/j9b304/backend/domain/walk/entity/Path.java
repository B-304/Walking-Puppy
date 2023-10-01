package io.ssafy.p.j9b304.backend.domain.walk.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "path")
@ToString
public class Path {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "path_id")
    private Long id;

    @Column(name = "path_type")
    private Integer pathType;

    @Column(name = "start_x")
    private Double startX;

    @Column(name = "start_y")
    private Double startY;

    @Column(name = "end_x")
    private Double endX;

    @Column(name = "end_y")
    private Double endY;

    private Double distance;

    @Column(name = "total_weight")
    private Double weight;



}
