package com.gandalp.gandalp.calender.domain.dto;

import com.gandalp.gandalp.hospital.domain.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDto {

    private Long roomId;

    private Status status;
}
