package com.gandalp.gandalp.config;

import com.gandalp.gandalp.hospital.domain.service.GeoCodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoStartupRunner implements ApplicationRunner {
    private final GeoCodingService geoCodingService;
    @Override
    public void run(ApplicationArguments args) {
        geoCodingService.convertAllHospitalAddressToGeo();
    }
}