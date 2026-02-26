package com.cms.cdl.mediclaim.mediclaim_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PremiumEstimateRequestDTO {

    private Integer sumInsured; // 300000 / 500000 / 700000

    private LocalDate employeeDob;

    private List<DependentInfoDTO> dependents;
}
