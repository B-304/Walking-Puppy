package io.ssafy.p.j9b304.backend.domain.dog.entity;

import io.ssafy.p.j9b304.backend.domain.dog.dto.DogModifyRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "dog")
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id", nullable = false)
    private Long dogId;

    @ColumnDefault("'dog'")
    @Column(name = "name", nullable = false)
    private String name;

    @ColumnDefault("0")
    @Column(name = "exp", nullable = false)
    private int exp;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

//    @OneToOne(mappedBy = "dog")
//    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_level_id")
    private DogLevel dogLevelId;


    @Builder
    public Dog(Long dogId, String name, int exp, LocalDateTime createdAt/*, User user */, DogLevel dogLevelId) {
        this.dogId = dogId;
        this.name = name;
        this.exp = exp;
        this.createdAt = createdAt;
//        this.user = user;
        this.dogLevelId = dogLevelId;
    }

    public void modifyDog(DogModifyRequestDto dogModifyRequestDto) {
        if (StringUtils.hasText(dogModifyRequestDto.getName()))
            this.name = dogModifyRequestDto.getName();
    }
}
