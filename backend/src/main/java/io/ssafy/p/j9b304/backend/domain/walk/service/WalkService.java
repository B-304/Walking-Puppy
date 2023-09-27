package io.ssafy.p.j9b304.backend.domain.walk.service;

import io.ssafy.p.j9b304.backend.domain.spot.entity.Spot;
import io.ssafy.p.j9b304.backend.domain.spot.repository.SpotRepository;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkAddRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkExistPathAddRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkModifyRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.WalkSaveRequestDto;
import io.ssafy.p.j9b304.backend.domain.walk.dto.response.WalkGetDetailResponseDto;
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
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    public WalkInitialInfoResponseDto addWalkNewPath(/* User user, */ WalkAddRequestDto walkAddRequestDto) {
        Theme theme = themeRepository.findById(walkAddRequestDto.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 없습니다."));

        Walk walk = walkAddRequestDto.toEntity(theme);
        Walk walkInit = walkRepository.save(walk);
//        walk.setUser(user);



        // 산책 스팟 저장
        List<Long> spotIdList = walkAddRequestDto.getSpotList();
        for (Long spotId : spotIdList) {
            Spot spot = spotRepository.findSpotById(spotId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 스팟이 없습니다."));
            walkSpotRepository.save(WalkSpot.builder().walk(walkInit).spot(spot).build());
        }

        // todo 추천 경로 생성 로직 추가
        // todo DB에 간식 스팟 추가

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

    public WalkInitialInfoResponseDto addWalkExistPath(WalkExistPathAddRequestDto walkExistPathAddRequestDto) {
        // todo user와 산책을 저장한 사용자가 같은지 확인
        Walk walkScrap = walkRepository.findById(walkExistPathAddRequestDto.getWalkId())
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        Walk walk = new Walk();

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

        // todo DB에 새로운 랜덤 간식 스팟 추가
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

    public List<Walk> getWalkList(/* User user, */) {
        // todo user id에 해당하는 산책 리스트만 가져오기
        return walkRepository.findByState('2');
    }

    public WalkGetDetailResponseDto getWalkDetail(/* User user, */Long walkId) {
        // todo user와 산책을 저장한 사용자가 같은지 확인
        Walk walk = walkRepository.findById(walkId)
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        List<Route> routeList = routeRepository.findByWalkAndState(walk, '1');
        List<WalkSpot> walkSpotList = walkSpotRepository.findByWalk(walk);
        List<Spot> spotList = new ArrayList<>();
        for (WalkSpot walkSpot : walkSpotList) {
            if (walkSpot.getSpot().getState() != '2') {
                spotList.add(walkSpot.getSpot());
            }
        }

        return WalkGetDetailResponseDto.builder()
                .walk(walk)
                .routeList(routeList)
                .spotList(spotList)
                .build();
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
        List<WalkSpot> walkSpotList = walkSpotRepository.findByWalk(walk);
        List<Spot> spotList = new ArrayList<>();
        for (WalkSpot walkSpot : walkSpotList) {
            if (walkSpot.getSpot().getState() != '2') {
                spotList.add(walkSpot.getSpot());
            }
        }

        // todo 강아지 경험치 증가
        return WalkSaveResponseDto.builder()
                .walk(walk)
                .routeList(addedRoute)
                .spotList(spotList)
                .exp(exp)
                .build();
    }

    public Walk scrapWalk(WalkModifyRequestDto walkModifyRequestDto) {
        // todo user와 산책을 저장한 사용자가 같은지 확인
        Walk walk = walkRepository.findById(walkModifyRequestDto.getWalkId())
                .orElseThrow(() -> new IllegalArgumentException("해당 산책이 없습니다."));

        if (walk.getState() == '0') {
            throw new IllegalArgumentException("아직 완료되지 않은 산책입니다.");
        } else if (walk.getState() == '2') {
            throw new IllegalArgumentException("이미 스크랩 된 산책입니다.");
        }

        walk.scrap(walkModifyRequestDto.getName());

        return walk;
    }

    public void addNewHadoopPath(double x, double y) {

        SparkSession spark = SparkSession.builder()
                .appName("Springtest")
                .master("spark://172.26.1.113:7077")
                .getOrCreate();
        System.out.println("=========================================================");
        System.out.println(spark);

        String dataFile = "hdfs://localhost:9000/data/CCTV_daejeon.csv";
        Dataset<Row> df = spark.read().format("csv")
                .option("header", "true")
                .option("inferSchema", "true")
                .load(dataFile);

        df.createOrReplaceTempView("police");

        long rowCount = df.count();
        System.out.println("police_road_distance의 행 수: " + rowCount);

        spark.stop();

    }
}

