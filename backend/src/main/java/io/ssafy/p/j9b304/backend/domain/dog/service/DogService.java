package io.ssafy.p.j9b304.backend.domain.dog.service;

//import io.ssafy.p.j9b304.backend.common.repository.ImageRepository;

import io.ssafy.p.j9b304.backend.domain.dog.dto.DogGetResponseDto;
import io.ssafy.p.j9b304.backend.domain.dog.dto.DogModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.dog.entity.Dog;
import io.ssafy.p.j9b304.backend.domain.dog.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DogService {
    private final DogRepository dogRepository;
//    private final ImageRepository imageRepository;

    @Transactional
    public void modifyDog( /*User user,*/ Long dogId, DogModifyRequestDto dogModifyRequestDto) {
        // todo : 사용자가 유효한지 검증
        Dog originaDog = dogRepository.findByDogId(dogId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강아지가 없습니다."));

        originaDog.modifyDog(dogModifyRequestDto);
    }

    public DogGetResponseDto getDog(Long dogId) {
        // todo : 사용자가 유효한지 검증
        Dog dog = dogRepository.findByDogId(dogId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강아지가 없습니다."));

//        Image image = imageRepository.findByImageId(dog.getDogLevelId().getImageId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 이미지가 없습니다."));

//        return dog.toResponse(image.getPath());
        return dog.toResponse();
    }
}
