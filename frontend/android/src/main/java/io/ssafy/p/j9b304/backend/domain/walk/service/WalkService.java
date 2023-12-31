package io.ssafy.p.j9b304.backend.domain.walk.service;

import io.ssafy.p.j9b304.backend.domain.dog.service.DogService;
import io.ssafy.p.j9b304.backend.domain.security.jwt.JwtTokenProvider;
import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import io.ssafy.p.j9b304.backend.domain.spot.repository.SpotRepository;
import io.ssafy.p.j9b304.backend.domain.user.entity.User;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.*;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkGetDetailResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkGetTodayResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkInitialInfoResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkSaveResponseDto;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Route;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Theme;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Walk;
import io.ssafy.p.j9b304.backend.domain.walk.entity.WalkSpot;
import io.ssafy.p.j9b304.backend.domain.walk.repository.RouteRepository;
import io.ssafy.p.j9b304.backend.domain.walk.repository.ThemeRepository;
import io.ssafy.p.j9b304.backend.domain.walk.repository.WalkRepository;
import io.ssafy.p.j9b304.backend.domain.walk.repository.WalkSpotRepository;
import io.ssafy.p.j9b304.backend.global.entity.File;
import io.ssafy.p.j9b304.backend.global.repository.FileRepository;
import io.ssafy.p.j9b304.backend.global.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WalkService {
    private final WalkRepository walkRepository;
    private final ThemeRepository themeRepository;
    private final RouteRepository routeRepository;
    private final SpotRepository spotRepository;
    private final WalkSpotRepository walkSpotRepository;
    private final FileRepository fileRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final DogService dogService;
    private final FileService fileService;
    private final RouteService routeService;

    public WalkInitialInfoResponseDto addWalkNewPath(HttpServletRequest httpServletRequest, WalkAddRequestDto walkAddRequestDto) {
        Theme theme = themeRepository.findById(walkAddRequestDto.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 없습니다."));

        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Walk walk = walkAddRequestDto.toEntity(theme);
        walk.setUser(walker);

        Walk walkInit = walkRepository.save(walk);
//        walk.setUser(user);

        // 산책 스팟 저장
        List<Long> spotIdList = walkAddRequestDto.getSpotList();
        if (spotIdList != null) {
            for (Long spotId : spotIdList) {
                Spot spot = spotRepository.findSpotById(spotId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 스팟이 없습니다."));
                walkSpotRepository.save(WalkSpot.builder().walk(walkInit).spot(spot).build());
            }
        }
        System.out.println(walkAddRequestDto.getStartLongitude() + ", " + walkAddRequestDto.getStartLatitude() + ", " +
                walkAddRequestDto.getEndLongitude() + ", " + walkAddRequestDto.getEndLatitude() + ", " +
                walkAddRequestDto.getEstimatedTime() + ", " + walkAddRequestDto.getThemeId());

        final Map<String, Object> resultRouteMap = routeService.getRecommendedRoute(walkAddRequestDto.getStartLongitude(), walkAddRequestDto.getStartLatitude(),
                walkAddRequestDto.getEndLongitude(), walkAddRequestDto.getEndLatitude(),
                walkAddRequestDto.getEstimatedTime(), walkAddRequestDto.getThemeId());
        List<Point> recommendedRoute = (List<Point>) resultRouteMap.get("recommendedRoute");

        walk.setEstimatedDistance((Double) resultRouteMap.get("totalDistance"));
        walk.setEstimatedTime((int) resultRouteMap.get("totalMinute"));

        char state = '0';
        int sequence = 1;
        if (recommendedRoute != null) {
            for (Point point : recommendedRoute) {
                Route route = Route.builder()
                        .state(state)
                        .sequence(sequence)
                        .latitude(point.getY())
                        .longitude(point.getX())
                        .walk(walk)
                        .build();

                routeRepository.save(route);
                sequence++;
            }
        } else {
            throw new IllegalArgumentException("경로를 생성할 수 없습니다.");
        }

        List<Route> routeList = routeRepository.findByWalkAndState(walkInit, '0');
        List<WalkSpot> walkSpotList = walkSpotRepository.findByWalk(walkInit);
        List<Spot> spotList = new ArrayList<>();
        List<Spot> itemSpotList = new ArrayList<>();
        for (WalkSpot walkSpot : walkSpotList) {
            if (walkSpot.getSpot().getState() == '2') {
                itemSpotList.add(walkSpot.getSpot());
            } else {
                spotList.add(walkSpot.getSpot());
            }
        }

        return WalkInitialInfoResponseDto.builder()
                .walk(walkInit)
                .routeList(routeList)
                .spotList(spotList)
                .itemSpotList(itemSpotList)
                .build();
    }

    public WalkInitialInfoResponseDto addWalkExistPath(HttpServletRequest httpServletRequest, WalkExistPathAddRequestDto walkExistPathAddRequestDto) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Walk walkScrap = walkRepository.findByWalkIdAndUser(walkExistPathAddRequestDto.getWalkId(), walker)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 선택한 산책과 일치하는 산책이 없습니다."));

        Walk walk = new Walk();
        walk.setUser(walker);

        List<Route> routeList = routeRepository.findByWalkAndState(walkScrap, '1');
        if (routeList.isEmpty()) {
            throw new IllegalArgumentException("해당 산책 기록이 없습니다.");
        }
        Route start = routeList.get(0);
        Route end = routeList.get(routeList.size() - 1);
        walk.walkExistPath(walkScrap, start, end);
        Walk walkInit = walkRepository.save(walk);

        List<WalkSpot> walkScrapSpotList = walkSpotRepository.findByWalk(walkScrap);
        for (WalkSpot walkSpot : walkScrapSpotList) {
            if (walkSpot.getSpot().getState() != '2') { // 간식 스팟이 아닌 경우 산책 스팟 저장
                walkSpotRepository.save(WalkSpot.builder().walk(walkInit).spot(walkSpot.getSpot()).build());
            }

        }

        List<WalkSpot> walkSpotList = walkSpotRepository.findByWalk(walkInit);
        List<Spot> spotList = new ArrayList<>();
        List<Spot> itemSpotList = new ArrayList<>();
        for (WalkSpot walkSpot : walkSpotList) {
            if (walkSpot.getSpot().getState() == '2') {
                itemSpotList.add(walkSpot.getSpot());
            } else {
                spotList.add(walkSpot.getSpot());
            }
        }

        return WalkInitialInfoResponseDto.builder()
                .walk(walkInit)
                .routeList(routeList)
                .spotList(spotList)
                .itemSpotList(itemSpotList)
                .build();
    }

    public List<Walk> getWalkList(HttpServletRequest httpServletRequest) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);
        return walkRepository.findByUserAndState(walker, '1');
    }

    public List<Walk> getWalkScrapList(HttpServletRequest httpServletRequest) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);
        return walkRepository.findByUserAndState(walker, '2');
    }

    public WalkGetDetailResponseDto getWalkDetail(HttpServletRequest httpServletRequest, Long walkId) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Walk walk = walkRepository.findByWalkIdAndUser(walkId, walker)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 선택한 산책과 일치하는 산책이 없습니다."));

        List<Route> routeList = routeRepository.findByWalkAndState(walk, '1');
        List<WalkSpot> walkSpotList = walkSpotRepository.findByWalk(walk);
        List<Spot> spotList = new ArrayList<>();
        for (WalkSpot walkSpot : walkSpotList) {
            if (walkSpot.getSpot().getState() != '2') {
                spotList.add(walkSpot.getSpot());
            }
        }

        File file = null;
        if (walk.getImageId() != null) {
            file = fileRepository.findById(walk.getImageId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 파일이 없습니다."));
        }

        return WalkGetDetailResponseDto.builder()
                .walk(walk)
                .routeList(routeList)
                .spotList(spotList)
                .file(file)
                .build();
    }

    public Walk modifyWalk(HttpServletRequest httpServletRequest, WalkModifyRequestDto walkModifyRequestDto) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Walk walk = walkRepository.findById(walkModifyRequestDto.getWalkId())
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        if (walker.getUserId() != walk.getUser().getUserId())
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");

        walk.setName(walkModifyRequestDto.getName());

        return walk;
    }

    public Walk removeWalk(HttpServletRequest httpServletRequest, Long walkId) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        if (walker.getUserId() != walk.getUser().getUserId())
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");

        walk.removeScrap();

        return walk;
    }


    public WalkSaveResponseDto saveWalk(HttpServletRequest httpServletRequest, WalkSaveRequestDto walkSaveRequestDto) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Walk walk = walkRepository.findByWalkIdAndUser(walkSaveRequestDto.getWalkId(), walker)
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        walk.walkOver(walkSaveRequestDto);
        Integer exp = walk.getWalkCount() / 1000; // 1000보 마다 경험치 1 증가
        exp += walkSaveRequestDto.getItemCount();

        List<Route> routeList = walkSaveRequestDto.getRoute().stream().map(r -> r.toEntity(walk, '1')).collect(Collectors.toList());
        List<Route> addedRoute = routeRepository.saveAll(routeList);
        List<WalkSpot> walkSpotList = walkSpotRepository.findByWalk(walk);
        List<Spot> spotList = new ArrayList<>();
        for (WalkSpot walkSpot : walkSpotList) {
            if (walkSpot.getSpot().getState() != '2') {
                spotList.add(walkSpot.getSpot());
            }
        }

        dogService.dogLevelUpCheck(walker.getDog().getDogId(), exp);

        return WalkSaveResponseDto.builder()
                .walk(walk)
                .routeList(addedRoute)
                .spotList(spotList)
                .exp(exp)
                .build();
    }

    public Walk scrapWalk(HttpServletRequest httpServletRequest, WalkModifyRequestDto walkModifyRequestDto/*, MultipartFile multipartFile*/) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        Walk walk = walkRepository.findById(walkModifyRequestDto.getWalkId())
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        if (walk.getState() == '0') {
            throw new IllegalArgumentException("아직 완료되지 않은 산책입니다.");
        } else if (walk.getState() == '2') {
            throw new IllegalArgumentException("이미 스크랩 된 산책입니다.");
        }

//        if (multipartFile != null) {
//            File file = fileService.addFile(multipartFile);
//            walk.setImageId(file.getFileId());
//        }

        walk.scrap(walkModifyRequestDto.getName());

        return walk;
    }

    public WalkGetTodayResponseDto getWalkToday(HttpServletRequest httpServletRequest) {
        User walker = jwtTokenProvider.extractUserFromToken(httpServletRequest);

        List<Walk> walks = walkRepository.findByUser(walker);

        int walkCount = 0;
        float walkDistance = 0;
        short walkCalorie = 0;

        for (Walk w : walks) {
            walkCount += w.getWalkCount();
            walkDistance += w.getDistance();
            walkCalorie += w.getCalorie();
        }


        return WalkGetTodayResponseDto.builder()
                .walkCount(walkCount)
                .distance(walkDistance)
                .calorie(walkCalorie)
                .build();
    }


}
