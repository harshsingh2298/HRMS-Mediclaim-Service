package com.cms.cdl.mediclaim.mediclaim_service.dto;

import lombok.*;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class PremiumEstimateResponseDTO {

    private BigDecimal estimatedPremium;
}

