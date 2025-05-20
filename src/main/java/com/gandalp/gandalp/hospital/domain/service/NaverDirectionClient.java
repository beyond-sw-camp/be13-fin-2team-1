package com.gandalp.gandalp.hospital.domain.service;

import com.gandalp.gandalp.hospital.domain.dto.DestinationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
@Service
@RequiredArgsConstructor
public class NaverDirectionClient {


    private final DirectionCacheService cacheService;

    // 25개씩 잘라서 direction 15 서비스 호출
    public Map<Long, Double> getRoadDistances(
            double latitude, double longitude,
            List<DestinationDto> destinations
    ) {

        Map<Long, Double> distances = new HashMap<>();
        final int BATCH = 25;

        for (int i = 0 ; i < destinations.size(); i += BATCH) {
            int end = Math.min(i + BATCH, destinations.size());

            for (int j = i ; j < end ; j ++){
                DestinationDto dest = destinations.get(j);
                double km = cacheService.getDistanceForPair(latitude, longitude, dest);

                if(km != Double.MAX_VALUE) {
                    distances.put(dest.getHospitalId(), km);
                }
            }
        }

        return distances;

    }
}




