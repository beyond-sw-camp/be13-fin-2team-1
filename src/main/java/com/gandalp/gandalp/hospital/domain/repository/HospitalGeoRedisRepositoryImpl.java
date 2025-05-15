package com.gandalp.gandalp.hospital.domain.repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HospitalGeoRedisRepositoryImpl implements HospitalGeoRedisRepository {

    // direction으로 도로 기준으로 가까운 응급실 아이디 20곳 조회


    private final RedisTemplate<String, String> redisTemplate;

    private static final String GEO_KEY = "hospital:geo";

    // 좌표로 바꾼 주소를 redis에 저장
    @Override
    public void saveHospitalLocation(Long hospitalId, double lon, double lat) {
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(lon, lat), hospitalId.toString());
    }

    // redis에서 가까운 병원 id 후보 50개 조회
    @Override
    public List<Long> findNearbyHospitalIds(double lat, double lon, int count){
        log.info("▶ findNearbyHospitalIds 호출: 위도={}, 경도={}, limit={}", lat, lon, count);
        Circle radius = new Circle(
                new Point(lon, lat),
                new Distance(20_000, RedisGeoCommands.DistanceUnit.KILOMETERS) // 반경 10km 내에서 검색
                // redis가 반경없이 조회하는 api를 제공하지 않음
        );

        GeoRadiusCommandArgs args = GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .limit(count);


        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo().radius(GEO_KEY, radius, args);

        if( results == null || results .getContent().isEmpty() ){
            log.warn("▶ Redis geo 조회 결과가 없습니다. 위도={}, 경도={}, limit={}", lat, lon, count);
        }else{
            log.info("▶ Redis geo 조회 결과 개수: {}", results.getContent().size());
            results.getContent().forEach(r -> {
                String id = r.getContent().getName();
                double distance = r.getDistance().getValue();
                log.info("   - 병원ID={} (거리={} {})", id, distance, r.getDistance().getUnit());
            });
        }
        return results.getContent().stream()
                .map(r -> Long.valueOf(r.getContent().getName())) // 저장된 병원 ID 추출
                .collect(Collectors.toList());
    }



    // 좌표 돌려주는 메서드
    @Override
    public List<Point> findLocationsByIds(List<Long> hospitalIds) {
        if (hospitalIds == null || hospitalIds.isEmpty()) {
            return Collections.emptyList();
        }
        // RedisGeoOperations.position()은 입력한 순서대로 Point 리스트를 돌려줍니다.
        String[] members = hospitalIds.stream()
                .map(String::valueOf)
                .toArray(String[]::new);
        List<Point> points = redisTemplate.opsForGeo()
                .position(GEO_KEY, members);
        // (null이 올 수도 있으니, 필요하다면 필터링 or 예외 처리)
        return points;
    }
}
