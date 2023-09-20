package io.ssafy.p.j9b304.backend.domain.spot.service;

import io.ssafy.p.j9b304.backend.domain.spot.dto.SpotAddRequestDto;
import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import io.ssafy.p.j9b304.backend.domain.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotService {
    private final SpotRepository spotRepository;

    @Transactional
    public void addSpot(SpotAddRequestDto spotAddRequestDto) {
        Spot spot = spotAddRequestDto.toEntity();

        spotRepository.save(spot);
    }
}
