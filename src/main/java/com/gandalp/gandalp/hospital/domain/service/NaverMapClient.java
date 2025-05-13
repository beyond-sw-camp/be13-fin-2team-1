package com.gandalp.gandalp.hospital.domain.service;

import com.gandalp.gandalp.hospital.domain.dto.GeoResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverMapClient {
    // 네이버 지도 api의 geocoding 서비스를 호출해 주소를 좌표( 위도, 경도 ) 로 변환

    /*
    * 1. 주소를 인코딩하여 URL 생성

      2. 네이버 지도 Geocoding API에 요청

      3. JSON 응답을 파싱

      4. 좌표(x, y)를 추출

      5. GeoResponse에 담아 반환
    *
    * */

    @Value("${naver.map.client-id}")
    private String clientId;

    @Value("${naver.map.client-secret}")
    private String clientSecret;

    // 주소를 좌표로 변환하는 메서드
    public GeoResponse getGeoPointFromAddress(String address) {

        HttpURLConnection conn = null;

        try {

            // 주소를 utf-8로 인코딩
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8)
                    .replace("+", "%20");

            // 요청 url 생성
            String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query="
                    + encodedAddress;

            log.debug("▶ Naver Geocode 요청 URL: {}", apiUrl);
            log.debug("▶ X-NCP-APIGW-API-KEY-ID: {}", clientId);
            log.debug("▶ X-NCP-APIGW-API-KEY: {}", clientSecret);

            // http 연결 설정
            conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            // 상태 코드 체크
            int status = conn.getResponseCode();
            if (status < 200 || status >= 300) {
                // 오류 응답 본문 로깅
                try (BufferedReader err = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errSb = new StringBuilder();
                    String errLine;
                    while ((errLine = err.readLine()) != null) {
                        errSb.append(errLine);
                    }
                    log.error("Naver Geocode API 오류: HTTP {} / body={}", status, errSb);
                }
                throw new EntityNotFoundException("네이버 지도 API 호출 실패: HTTP " + status);
            }

            // 정상 응답 읽기
            StringBuilder sb = new StringBuilder();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
            }
            //json 파싱
            JSONObject result = new JSONObject(sb.toString());
            JSONArray addresses = result.getJSONArray("addresses");

            // 주소 결과가 없는 경우 예외
            if(addresses.isEmpty()){
                throw new EntityNotFoundException("해당 주소에 대한 좌표 정보를 찾을 수 없습니다." + address);
            }

            // 좌표 추출
            JSONObject first = addresses.getJSONObject(0);
            double longitude = Double.parseDouble(first.getString("x"));
            double latitude  = Double.parseDouble(first.getString("y"));

            return new GeoResponse(longitude, latitude);
        } catch (EntityNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("NaverMapClient 처리 중 예외 발생", e);
            throw new RuntimeException("지도 정보 조회 실패", e);
        }finally{
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}