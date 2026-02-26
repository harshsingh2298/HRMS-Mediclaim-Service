package com.cms.cdl.mediclaim.mediclaim_service.service.impl;

import com.cms.cdl.mediclaim.mediclaim_service.dto.PremiumEstimateRequestDTO;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimEnrollmentEntity;

import java.math.BigDecimal;

public interface PremiumCalculationService {
    BigDecimal calculate(PremiumEstimateRequestDTO request);
}
