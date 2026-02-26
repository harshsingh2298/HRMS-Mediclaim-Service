package com.cms.cdl.mediclaim.mediclaim_service.controller;

import com.cms.cdl.mediclaim.mediclaim_service.dto.PremiumEstimateRequestDTO;
import com.cms.cdl.mediclaim.mediclaim_service.dto.PremiumEstimateResponseDTO;
import com.cms.cdl.mediclaim.mediclaim_service.service.impl.PremiumCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/mediclaim/premium")
@RequiredArgsConstructor
public class PremiumController {

    private final PremiumCalculationService premiumService;

    @PostMapping("/estimate")
    public PremiumEstimateResponseDTO estimate(
            @RequestBody PremiumEstimateRequestDTO request) {

        BigDecimal premium =
                premiumService.calculate(request);

        return new PremiumEstimateResponseDTO(premium);
    }

}
