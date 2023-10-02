package io.ssafy.p.j9b304.backend.domain.dog.service;

//import io.ssafy.p.j9b304.backend.common.repository.ImageRepository;

import io.ssafy.p.j9b304.backend.domain.dog.dto.DogGetResponseDto;
import io.ssafy.p.j9b304.backend.domain.dog.dto.DogModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.dog.entity.Dog;
import io.ssafy.p.j9b304.backend.domain.dog.entity.DogLevel;
import io.ssafy.p.j9b304.backend.domain.dog.repository.DogLevelRepository;
import io.ssafy.p.j9b304.backend.domain.dog.repository.DogRepository;
import io.ssafy.p.j9b304.backend.domain.security.jwt.JwtTokenProvider;
import io.ssafy.p.j9b304.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DogService {
    private final DogRepository dogRepository;
    private final DogLevelRepository dogLevelRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void modifyDog(HttpServletRequest httpServletRequest, Long dogId, DogModifyRequestDto dogModifyRequestDto) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Dog originaDog = dogRepository.findByDogId(dogId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강아지가 없습니다."));

        originaDog.modifyDog(dogModifyRequestDto);
    }

    public DogGetResponseDto getDog(Long dogId) {
        Dog dog = dogRepository.findByDogId(dogId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강아지가 없습니다."));

//        Image image = imageRepository.findByImageId(dog.getDogLevelId().getImageId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 이미지가 없습니다."));

//        return dog.toResponse(image.getPath());
        return dog.toResponse();
    }

    @Transactional
    public Dog addDog() {
        DogLevel dogLevel = dogLevelRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 강아지 레벨이 없습니다."));

        Dog newDog = Dog.builder()
                .name("뽀삐")
                .dogLevelId(dogLevel)
                .exp(0)
                .createdAt(LocalDateTime.now())
                .build();

        dogRepository.save(newDog);

        return newDog;
    }

    @Transactional
    public void dogLevelUpCheck(Long dogId, Integer exp) {
        Dog dog = dogRepository.findByDogId(dogId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 강아지가 없습니다."));

        Integer newExp = dog.getExp() + exp;
        dog.setExp(newExp);

        if (50 <= newExp && newExp < 120)
            dog.setDogLevelId(dogLevelRepository.findByDogLevelId(2L));
        else if (120 <= newExp && newExp < 200)
            dog.setDogLevelId(dogLevelRepository.findByDogLevelId(3L));
        else if (200 <= newExp && newExp < 300)
            dog.setDogLevelId(dogLevelRepository.findByDogLevelId(4L));
        else if (newExp > 300)
            dog.setDogLevelId(dogLevelRepository.findByDogLevelId(5L));
    }
}
