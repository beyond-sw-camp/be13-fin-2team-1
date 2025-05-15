package com.gandalp.gandalp.hospital.domain.service;

import com.gandalp.gandalp.hospital.domain.dto.DestinationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverDirectionClient {


    @Value("${naver.map.client-id}")
    private String clientId;

    @Value("${naver.map.client-secret}")
    private String clientSecret;


    public Map<Long, Double> getRoadDistances(double lat,
                                              double lon,
                                              List<DestinationDto> destinations) {
        Map<Long, Double> distances = new HashMap<>();
        // Naver Direction API는 한 번에 최대 25개 목적지 호출
        final int BATCH_SIZE = 25;

        for (int i = 0; i < destinations.size(); i += BATCH_SIZE) {
            List<DestinationDto> batch = destinations.subList(i, Math.min(i + BATCH_SIZE, destinations.size()));
            for (DestinationDto dest : batch) {
                try {
                    // 인코딩 전 좌표 문자열
                    String startCoord = lon + "," + lat;
                    String goalCoord  = dest.getLongitude() + "," + dest.getLatitude();

                    // 전체 파라미터값을 URLEncoder
                    String apiUrl = String.format(
                            "https://naveropenapi.apigw.ntruss.com/map-direction/v2/driving" +
                                    "?start=%s&goal=%s&option=trafast",
                            URLEncoder.encode(startCoord, StandardCharsets.UTF_8),
                            URLEncoder.encode(goalCoord,  StandardCharsets.UTF_8)
                    );

                    log.debug("▶ 요청 URL: {}", apiUrl);


                    HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
                    conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

                    int status = conn.getResponseCode();

                    log.info("▶ 응답 상태 코드(status) = {}", status);
                    if (status < 200 || status >= 300) {
                        log.error("▶ Naver Direction API 오류 status={}, url={}", status, apiUrl);
                        conn.disconnect();
                        continue;
                    }

                    // 응답 읽기
                    StringBuilder sb = new StringBuilder();
                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                        }
                    }
                    conn.disconnect();

                    // JSON 파싱: route.summary.distance (미터 단위)
                    JSONObject root = new JSONObject(sb.toString());
                    log.debug("▶ 응답 바디: {}", root);
                    JSONObject route = root.getJSONObject("route");
                    JSONObject summary = route.getJSONObject("summary");
                    double distMeters = summary.getDouble("distance");
                    double distKm = distMeters / 1000.0;

                    distances.put(dest.getHospitalId(), distKm);
                } catch (Exception ex) {
                    log.error("▶ NaverDirectionClient 예외: 목적지={} ", dest, ex);
                }
            }
        }

        return distances;
    }

}
