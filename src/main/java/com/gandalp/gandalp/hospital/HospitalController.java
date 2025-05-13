package com.gandalp.gandalp.hospital;

import com.gandalp.gandalp.hospital.domain.dto.ErCountUpdateDto;
import com.gandalp.gandalp.hospital.domain.dto.HospitalDto;
import com.gandalp.gandalp.hospital.domain.dto.HospitalErResponseDto;
import com.gandalp.gandalp.hospital.domain.entity.Hospital;
import com.gandalp.gandalp.hospital.domain.entity.SortOption;
import com.gandalp.gandalp.hospital.domain.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;
    private final SimpMessagingTemplate messagingTemplate;


    // 현재 위치에서 가까운 순으로 조회하려면  프론트에서 현재 위치를 보내줘야 함
    @Operation(summary = "응급실 병상 수용 정보 조회", description = "수용 가능한 병상 수 정보 거리 순으로 20개 조회")
    @GetMapping
    public ResponseEntity<?> getHospitals(
            @RequestParam double lat,
            @RequestParam double lon, // 현재 위치
            @RequestParam SortOption sortOption,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, page = 0) Pageable pageable ) {


        Page<HospitalDto> hospitalList = null;

        try {
            hospitalList = hospitalService.getNearestHospitals(lon, lat, keyword, sortOption, pageable);

        }catch (Exception e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(hospitalList);
    }


    // 응급실 가용 병상 수 수정
    @Operation(summary = "응급실 가용 병상 수 수정", description = "응급실 가용 병상 수를 간호사가 직접 수정할 수 있다.")
    @PostMapping
    @PreAuthorize("hasRole('NURSE')")
    public ResponseEntity<?> updateErCount(@Valid @RequestBody ErCountUpdateDto dto) {

        HospitalErResponseDto resDto = null;

        try{
            // DB 업데이트
            resDto = hospitalService.updateErCount(dto);


            // 변경된 병상 수를 Stomp 채널로 브로드캐스트 해줌 -> 새로고침하지 않아도 자동으로 확인 가능
            messagingTemplate.convertAndSend("/topic/er", resDto);


        }catch(Exception e) {
            ResponseEntity.badRequest().body(e.getMessage());
        }


        return ResponseEntity.ok(resDto);
    }

    // 조회

    // 검색

    // 수용 정보

}
