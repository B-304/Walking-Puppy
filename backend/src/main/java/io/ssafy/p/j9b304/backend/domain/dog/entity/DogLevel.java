package io.ssafy.p.j9b304.backend.domain.dog.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "dog_level")
public class DogLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_level_id", nullable = false)
    private Long dogLevelId;

    @Column(name = "level", nullable = false)
    private char level;

    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Column(name = "level_range", nullable = false)
    private int levelRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_type_id")
    private DogType dogTypeId;
}
