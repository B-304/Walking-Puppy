package io.ssafy.p.j9b304.backend.domain.dog.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "dog_type")
public class DogType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_type_id", nullable = false)
    private Long dogTypeId;

    @Column(name = "type", nullable = false)
    private String type;
}
