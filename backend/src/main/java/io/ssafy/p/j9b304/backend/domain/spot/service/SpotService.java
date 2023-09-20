package io.ssafy.p.j9b304.backend.domain.spot.service;

import io.ssafy.p.j9b304.backend.domain.spot.dto.AddRequestDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.GetHotSpotResponseDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.GetResponseDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.ModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import io.ssafy.p.j9b304.backend.domain.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotService {
    private final SpotRepository spotRepository;

    @Transactional
    public void addSpot(AddRequestDto addRequestDto) {
        Spot spot = addRequestDto.toEntity();

        spotRepository.save(spot);
    }

    @Transactional
    public void modifySpot(Long spotId, ModifyRequestDto modifyRequestDto) {
        Spot originalSpot = spotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스팟이 없습니다."));

        // todo : 사용자 검증

        originalSpot.modifySpot(modifyRequestDto);
    }

    @Transactional
    public void removeSpot(Long spotId) {
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스팟이 없습니다."));

        // todo : 사용자 검증

        spotRepository.delete(spot);
    }

    public List<GetResponseDto> getSpotList() {
        List<Spot> spotList = spotRepository.findAll();

        return spotList.stream().map(Spot::toDto).collect(Collectors.toList());
    }

    public GetResponseDto getSpotDetail(Long spotId) {
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스팟이 없습니다."));

        return spot.toDto();
    }

    public List<GetHotSpotResponseDto> getHotSpotList() {
        List<Spot> spotList = spotRepository.findHotSpotAll();

        return spotList.stream().map(Spot::toHotSpotDto).collect(Collectors.toList());
    }
}
