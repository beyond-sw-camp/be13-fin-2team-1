package com.gandalp.gandalp.hospital.domain.service;

import com.gandalp.gandalp.hospital.domain.dto.DestinationDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class DirectionCacheService {

    // cacheable은 같은 클래스 내부에서 자기 메서드 호출 시 동작하지 않아서
    // 별도 빈으로 분리

    @Value("${naver.map.client-id}")
    private String clientId;

    @Value("${naver.map.client-secret}")
    private String clientSecret;

    private static final String OPTION  = "trafast";


    // 도로상 실제 거리 가져오기
    // 캐시 적용
    // 한번 계산한 origin + destination 쌍을 redis에 저장해 해당 구간은 네이버 api 호출하지 않도록 함

    /*
     * 목적지 리스트(50개)를 최대 25개씩 나눠서
     * 캐시에서 거리를 꺼냄(있으면 재사용)
     * 없으면 네이버 api 호출하고 캐시에 저장
     * 병원과 현재 위치에서의 거리를 돌려줌
     * */


    // 거리 계산만 담당 ( redis에 저장 )
    @Cacheable( cacheNames = "routeDistances",
            key = "#longitude + ',' + #latitude + ':' + #dest.longitude+ ',' + #dest.latitude"
    )
    public double getDistanceForPair( double latitude, double longitude, DestinationDto dest) {
        String start = longitude + "," + latitude;
        String goal  = dest.getLongitude() + "," + dest.getLatitude();
        String url   = "https://maps.apigw.ntruss.com/map-direction-15/v1/driving"
                + "?start=" + URLEncoder.encode(start, StandardCharsets.UTF_8)
                + "&goal="  + URLEncoder.encode(goal,  StandardCharsets.UTF_8)
                + "&option=" + OPTION;
        try {
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY",    clientSecret);

            if(conn.getResponseCode() != 200){
                conn.disconnect();
                return Double.MAX_VALUE;
            }


            StringBuilder sb = new StringBuilder();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) sb.append(line);
            }
            conn.disconnect();

            JSONObject root     = new JSONObject(sb.toString());
            JSONObject routeObj = root.getJSONObject("route");
            JSONArray arr      = routeObj.getJSONArray(OPTION);
            if (arr.isEmpty()) return Double.MAX_VALUE;

            JSONObject summary = arr.getJSONObject(0).getJSONObject("summary");
            return summary.getDouble("distance") / 1000.0;

        } catch (Exception ex) {
            log.error("Direction API 예외: dest={}", dest, ex);
            return Double.MAX_VALUE;
        }
    }


}
