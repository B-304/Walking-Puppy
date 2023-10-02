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


    public Map<String, Object> getRecommendedRoute(double startLongitude, double startLatitude, double endLongitude, double endLatitude, Integer estimatedTime, Long themeType) {

        Map<String,Object> map = new HashMap<>();
        List<Point> combinedPath = new ArrayList<>();
        double totalDistance =0;

        double r = (estimatedTime * 80.0 * 0.6) / 1000;
        List<Path> safePaths = safePathRepository.findSafePathsInRadius(startLongitude, startLatitude, r * 0.8, themeType.intValue());

        Map<Point, List<Edge>> graph = convertPathsToGraph(safePaths);

        Point closestStart = findClosestKey(graph, startLongitude, startLatitude);
        System.out.println("closetSatrt= " + closestStart);

        Map<Point, DijkstraResult> shortestPaths = dikjstra(graph, closestStart);

        double targetKm = (estimatedTime / 2.0 * 80) / 1000; //분속 80m라고 가정함
        PathResult pathFromStartToTarget = findShortestPathOverTargetKm(shortestPaths, targetKm);
        System.out.println(pathFromStartToTarget);

        DijkstraResult targetResult = pathFromStartToTarget.getDestination();
        if (targetResult == null) {
            System.out.println("목적지가 없음");
            return null;
        }
        totalDistance+=targetResult.getTotalDistance();


        Point Closestend = findClosestKey(graph, endLongitude, endLatitude);
        Map<Point, DijkstraResult> reverseShortestPaths = dikjstra(graph, targetResult.getPoint());
        PathResult pathFromTargetToStart = findPathToStart(reverseShortestPaths, Closestend);


        //도착지로 되돌아 오는 경로
        DijkstraResult returnResult = pathFromTargetToStart.getDestination();
        if (returnResult == null) {
            System.out.println("시작점으로 되돌아 오는 경로 없음");

            //왔던 길로 되돌아 온다.
            if (startLongitude == endLongitude && startLatitude == endLatitude) {
                for (Point p : pathFromStartToTarget.getPath()) {
                    combinedPath.add(p);
                }
                for (int i = pathFromStartToTarget.getPath().size() - 2; i >= 0; i--) {
                    combinedPath.add(pathFromStartToTarget.getPath().get(i));

                }
                totalDistance+=targetResult.getTotalDistance();

            } else {
                return null;
            }
        } else {
            combinedPath.addAll(pathFromStartToTarget.getPath());
            combinedPath.remove(combinedPath.size() - 1);
            combinedPath.addAll(pathFromTargetToStart.getPath());
            totalDistance+=returnResult.getTotalDistance();
        }

        map.put("recommendedRoute", combinedPath);
        map.put("totalDistance", totalDistance);

        Integer totalMinute =  (int)(totalDistance*1000/80);
        map.put("totalMinute", totalMinute);

        return map;
    }


    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    private static double calculateHaversineDistance(Point p1, double lon2, double lat2) {

        double dLat = Math.toRadians((lat2 - p1.getY()));
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
            Double distance = calculateHaversineDistance(point, nowX, nowY);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }
        return closestPoint;
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

        return new PathResult(null, Collections.emptyList());
    }

    private PathResult findShortestPathOverTargetKm(Map<Point, DijkstraResult> shortestPaths, double targetKm) {
        DijkstraResult minWeightOverTargetKm = null;

        for (DijkstraResult result : shortestPaths.values()) {
            //  if (result.getTotalDistance().compareTo(targetKm) >0) {
            if (result.getTotalDistance() > targetKm) {
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

        return new PathResult(null, Collections.emptyList());
    }

    private Map<Point, DijkstraResult> dikjstra(Map<Point, List<Edge>> graph, Point start) {

        //가중치가 작은 것부터 꺼내는 다익스트라
        PriorityQueue<DijkstraResult> queue = new PriorityQueue<>(Comparator.comparingDouble(DijkstraResult::getTotalWeight));
//        PriorityQueue<DijkstraResult> queue = new PriorityQueue<>(Comparator.comparing(DijkstraResult::getTotalWeight));
        Map<Point, DijkstraResult> results = new HashMap<>();

        queue.add(new DijkstraResult(start, 0.0, 0.0, null));

        while (!queue.isEmpty()) {
            DijkstraResult current = queue.poll();
            if (results.containsKey(current.getPoint())) continue;

            results.put(current.getPoint(), current);

            for (Edge edge : graph.getOrDefault(current.getPoint(), Collections.emptyList())) {
                if (!results.containsKey(edge.getTarget())) {
                    DijkstraResult next = new DijkstraResult(edge.getTarget(),
                            current.getTotalWeight() + edge.getWeight(),
                            current.getTotalDistance() + edge.getDistance(),
                            current);

                    queue.add(next);
                }
            }
        }

        return results;

    }

    private Map<Point, List<Edge>> convertPathsToGraph(List<Path> paths) {

        Map<Point, List<Edge>> graph = new HashMap<>();

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
