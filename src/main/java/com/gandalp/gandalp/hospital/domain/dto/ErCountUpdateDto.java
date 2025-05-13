package com.gandalp.gandalp.hospital.domain.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErCountUpdateDto {

    private Long hospitalId;

    @Min(0)
    private int availableErCount;
}
