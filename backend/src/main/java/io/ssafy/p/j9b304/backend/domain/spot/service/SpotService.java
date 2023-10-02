package io.ssafy.p.j9b304.backend.domain.spot.service;

import io.ssafy.p.j9b304.backend.domain.security.jwt.JwtTokenProvider;
import io.ssafy.p.j9b304.backend.domain.spot.dto.AddRequestDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.GetHotSpotResponseDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.GetResponseDto;
import io.ssafy.p.j9b304.backend.domain.spot.dto.ModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import io.ssafy.p.j9b304.backend.domain.spot.repository.SpotRepository;
import io.ssafy.p.j9b304.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotService {
    private final SpotRepository spotRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void addSpot(HttpServletRequest httpServletRequest, AddRequestDto addRequestDto) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Spot spot = addRequestDto.toEntity();
        spot.setUser(walker);

        spotRepository.save(spot);
    }

    @Transactional
    public void modifySpot(HttpServletRequest httpServletRequest, Long spotId, ModifyRequestDto modifyRequestDto) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Spot originalSpot = spotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스팟이 없습니다."));

        if (walker.getUserId() != originalSpot.getUser().getUserId())
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");

        originalSpot.modifySpot(modifyRequestDto);
    }

    @Transactional
    public void removeSpot(HttpServletRequest httpServletRequest, Long spotId) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스팟이 없습니다."));

        if (walker.getUserId() != spot.getUser().getUserId())
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");

        spotRepository.delete(spot);
    }

    public List<GetResponseDto> getSpotList(HttpServletRequest httpServletRequest) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        List<Spot> spotList = spotRepository.findSpotAllByUser(walker);

        return spotList.stream().map(Spot::toSpotDto).collect(Collectors.toList());
    }

    public GetResponseDto getSpotDetail(HttpServletRequest httpServletRequest, Long spotId) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Spot spot = spotRepository.findSpotByIdAndUser(spotId, walker)
                .orElseThrow(() -> new IllegalArgumentException("해당 스팟이 없습니다."));

        return spot.toSpotDto();
    }

    public List<GetHotSpotResponseDto> getHotSpotList() {
        List<Spot> spotList = spotRepository.findHotSpotAll();

        return spotList.stream().map(Spot::toHotSpotDto).collect(Collectors.toList());
    }

    public GetHotSpotResponseDto getHotSpotDetail(Long spotId) {
        Spot spot = spotRepository.findHotSpotByID(spotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 스팟이 없습니다."));

        return spot.toHotSpotDto();
    }
}
