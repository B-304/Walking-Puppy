package io.ssafy.p.j9b304.backend.domain.walk.service;

import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkAddRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkSaveRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkGetDetailResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkSaveResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Route;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Theme;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.ssafy.p.j9b304.backend.domain.walk.repository.RouteRepository;
import io.ssafy.p.j9b304.backend.domain.walk.repository.ThemeRepository;
import io.ssafy.p.j9b304.backend.domain.walk.repository.WalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WalkService {
    private final WalkRepository walkRepository;
    private final ThemeRepository themeRepository;
    private final RouteRepository routeRepository;

    public Walk addWalkNewPath(/* User user, */ WalkAddRequestDto walkAddRequestDto) {
        Theme theme = themeRepository.findById(walkAddRequestDto.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 없습니다."));

        Walk walk = walkAddRequestDto.toEntity(theme);
//        walk.setUser(user);

        return walkRepository.save(walk);
    }


    public List<Walk> getWalkList(/* User user, */) {
        // todo user id에 해당하는 산책 리스트만 가져오기
        return walkRepository.findByState('2');
    }

    public WalkGetDetailResponseDto getWalkDetail(/* User user, */Long walkId) {
        // todo user와 산책을 저장한 사용자가 같은지 확인
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        List<Route> routeList = routeRepository.findByWalkAndState(walk, '1');
        WalkGetDetailResponseDto walkGetDetailResponseDto = new WalkGetDetailResponseDto(walk, routeList);
        return walkGetDetailResponseDto;
    }

    public Walk modifyWalk(WalkModifyRequestDto walkModifyRequestDto) {
        // todo user와 산책을 저장한 사용자가 같은지 확인
        Walk walk = walkRepository.findById(walkModifyRequestDto.getWalkId())
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));
        walk.setName(walkModifyRequestDto.getName());

        return walk;
    }

    public Walk removeWalk(/* User user, */Long walkId) {

        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        // todo user와 산책을 저장한 사용자가 같은지 확인
        walk.removeScrap();

        return walk;
    }


    public WalkSaveResponseDto saveWalk(WalkSaveRequestDto walkSaveRequestDto) {
        Walk walk = walkRepository.findById(walkSaveRequestDto.getWalkId())
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        walk.walkOver(walkSaveRequestDto);
        Integer exp = walk.getWalkCount() / 1000; // 1000보 마다 경험치 1 증가
        exp += walkSaveRequestDto.getItemCount();

        List<Route> routeList = walkSaveRequestDto.getRoute().stream().map(r -> r.toEntity(walk, '1')).collect(Collectors.toList());
        List<Route> addedRoute = routeRepository.saveAll(routeList);

        // todo 강아지 경험치 증가
        WalkSaveResponseDto walkSaveResponseDto = new WalkSaveResponseDto(walk, addedRoute, exp);
        return walkSaveResponseDto;
    }

    public Walk modifyWalkState(Long walkId) {
        // todo user와 산책을 저장한 사용자가 같은지 확인
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        if (walk.getState() != '1') {
            // todo 예외 처리 (산책 완료되지 않은 경우 또는 이미 스크랩 된 경우)
            throw new RuntimeException();
        }

        walk.scrap();

        return walk;
    }
}
