package io.ssafy.p.j9b304.backend.domain.walk.service;

import io.ssafy.p.j9b304.backend.domain.walk.dto.request.DijkstraResult;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.Edge;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.PathResult;
import io.ssafy.p.j9b304.backend.domain.walk.dto.request.Point;
import io.ssafy.p.j9b304.backend.domain.walk.entity.Path;
import io.ssafy.p.j9b304.backend.domain.walk.repository.SafePathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteService {


    private final SafePathRepository safePathRepository;


    public List<Point> getSafeRoute(double nowX, double nowY, Integer estimatedTime, Integer type){

        double r = (estimatedTime*80.0)/1000;

        //현재 점에서 반경 r의 시작점을 가진 모든 간선을 가져온다.
        List<Path> safePaths = safePathRepository.findSafePathsInRadius(nowX, nowY, r, type);

        System.out.println(safePaths.size());
//        for(Path tmp: safePaths){
//            System.out.println(tmp.getStartX()+", "+tmp.getStartY()+", "+tmp.getEndX()+", "+tmp.getEndY());
//        }

        //간선정보로 그래프를 생성하낟.
        Map<Point, List<Edge>> graph = convertPathsToGraph(safePaths);

        //출발지와 가장 가까운 시작점
        Point closestStart = findClosestKey(graph, nowX, nowY);
        System.out.println("closetSatrt= "+ closestStart);


        //모든 점들에 대하여 최소 가중치를 구함
        Map<Point, DijkstraResult> shortestPaths = dikjstra(graph,closestStart);
        System.out.println(shortestPaths.size());

        for (Map.Entry<Point, DijkstraResult> entrySet : shortestPaths.entrySet()) {
            System.out.println(entrySet.getKey() + " : " + entrySet.getValue().getTotalDistance()+", "+entrySet.getValue().getTotalWeight());
        }


        double targetKm = (estimatedTime/2.0*80)/1000; //분속 80m라고 가정함
        System.out.println("targetkm= "+targetKm);

        //targetKm넘는 경로 중에 가중치가 가장 작은 점과 경로를 반환함
        PathResult pathFromStartToTarget  = findShortestPathOverTargetKm(shortestPaths, targetKm);
        System.out.println(pathFromStartToTarget);

        //여기서 값이 null이면 해당 출발지에서 시간안에 갈 수 있는 점이 없다는 것
        DijkstraResult targetResult = pathFromStartToTarget.getDestination();
        if(targetResult==null) {
            System.out.println("목적지가 없음");
            return null;
        }

        System.out.println("===================리턴 점에 그래프가 있는 지 확인!=============");
        System.out.println(graph.get(targetResult.getPoint()));


        Map<Point,DijkstraResult> reverseShortestPaths = dikjstra(graph,targetResult.getPoint());
        System.out.println("리턴 다익스트라 결과");
        System.out.println(reverseShortestPaths);

        PathResult pathFromTargetToStart=findPathToStart(reverseShortestPaths ,closestStart);
        System.out.println(reverseShortestPaths.get(closestStart));

        //여기서 값이 null이면 시작점으로 되돌아 올 수 있는 경로가 없다는 것
        DijkstraResult startResult = pathFromTargetToStart.getDestination();
        if(startResult==null) {
            System.out.println("시작점으로 되돌아 오는 경로 없음");
            //return null;

            Point closestEnd = findClosestKey(reverseShortestPaths, closestStart.getX(), closestStart.getY());
             pathFromTargetToStart=findPathToStart(reverseShortestPaths ,closestEnd);
            System.out.println(reverseShortestPaths.get(closestStart));
            startResult = pathFromTargetToStart.getDestination();

        }



        List<Point> combinedPath = new ArrayList<>();
        System.out.println("=====최종 경로===");
        System.out.println(pathFromStartToTarget.getPath());
        System.out.println(pathFromTargetToStart.getPath());

        combinedPath.addAll(pathFromStartToTarget.getPath());
        combinedPath.addAll(pathFromTargetToStart.getPath());

        System.out.println(combinedPath);

        return combinedPath;
//

    }


    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    private static double calculateHaversineDistance(Point p1, double lon2, double lat2) {

        double dLat  = Math.toRadians((lat2 - p1.getY()));
        double dLong = Math.toRadians((lon2 - p1.getX()));

        lat2 = Math.toRadians(lat2);

        double lat1 = Math.toRadians(p1.getY());

        double a = haversin(dLat) + Math.cos(lat1) * Math.cos(lat2) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    private static Point findClosestKey(Map<Point, ?> graph, double nowX, double nowY) {

        if (graph == null || graph.isEmpty()) {
            throw new IllegalArgumentException("Graph cannot be null or empty");
        }

        Double minDistance = Double.MAX_VALUE;
        Point closestPoint = null;

        for (Point point : graph.keySet()) {
            Double distance = calculateHaversineDistance(point , nowX , nowY);
            if(distance < minDistance){
                minDistance=distance ;
                closestPoint=point ;
            }
        }
        return closestPoint ;
    }


    private static Double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
    private PathResult findPathToStart(Map<Point, DijkstraResult> shortestPaths, Point source) {
        DijkstraResult sourceResult = shortestPaths.get(source);

        if (sourceResult != null) {
            List<Point> path = new ArrayList<>();
            for (DijkstraResult node = sourceResult; node != null; node = node.getPrevious()) {
                path.add(node.getPoint());
            }

            Collections.reverse(path);
            return new PathResult(sourceResult, path);
        }

        return new PathResult(null,Collections.emptyList());
    }

    private PathResult findShortestPathOverTargetKm(Map<Point, DijkstraResult> shortestPaths, double targetKm) {
        DijkstraResult minWeightOverTargetKm = null;

        for (DijkstraResult result : shortestPaths.values()) {
          //  if (result.getTotalDistance().compareTo(targetKm) >0) {
            if(result.getTotalDistance() > targetKm){
                if (minWeightOverTargetKm == null || result.getTotalWeight() < minWeightOverTargetKm.getTotalWeight()) {
                //if (minWeightOverTargetKm == null || result.getTotalWeight().compareTo(minWeightOverTargetKm.getTotalWeight()) < 0) {

                    minWeightOverTargetKm = result;
                }
            }
        }

        if (minWeightOverTargetKm != null) {
            List<Point> path = new ArrayList<>();
            for (DijkstraResult node = minWeightOverTargetKm; node != null; node = node.getPrevious()) {
                path.add(node.getPoint());
            }

            Collections.reverse(path);

            return new PathResult(minWeightOverTargetKm, path);
        }

        return new PathResult(null,Collections.emptyList());
    }

    private Map<Point, DijkstraResult> dikjstra(Map<Point, List<Edge>> graph, Point start) {

        //가중치가 작은 것부터 꺼내는 다익스트라
       PriorityQueue<DijkstraResult> queue = new PriorityQueue<>(Comparator.comparingDouble(DijkstraResult::getTotalWeight));
//        PriorityQueue<DijkstraResult> queue = new PriorityQueue<>(Comparator.comparing(DijkstraResult::getTotalWeight));
        Map<Point, DijkstraResult> results = new HashMap<>();

        queue.add(new DijkstraResult(start,0.0, 0.0,null));

        while (!queue.isEmpty()) {
            DijkstraResult current = queue.poll();
            if (results.containsKey(current.getPoint())) continue;

            results.put(current.getPoint(), current);

            for (Edge edge : graph.getOrDefault(current.getPoint(), Collections.emptyList())) {
                if (!results.containsKey(edge.getTarget())) {
                    DijkstraResult next = new DijkstraResult(edge.getTarget(),
                            current.getTotalWeight() + edge.getWeight(),
                            current.getTotalDistance()+ edge.getDistance(),
                            current);

                    queue.add(next);
                }
            }
        }

        return results;

    }

    private Map<Point, List<Edge>> convertPathsToGraph(List<Path> paths) {

        Map<Point,List<Edge>> graph = new HashMap<>();

        for (Path path : paths) {
            Point startPoint = new Point(path.getStartX(), path.getStartY());
            Point endPoint = new Point(path.getEndX(), path.getEndY());
            Edge edge = new Edge(endPoint, path.getWeight(), path.getDistance());

            if (!graph.containsKey(startPoint)) {
                graph.put(startPoint, new ArrayList<>());
            }

            graph.get(startPoint).add(edge);
        }

        return graph;

    }

}
