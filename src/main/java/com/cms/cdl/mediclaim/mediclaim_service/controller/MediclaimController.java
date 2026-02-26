package com.cms.cdl.mediclaim.mediclaim_service.controller;

import com.cms.cdl.mediclaim.mediclaim_service.dto.PolicyHighlightResponseDTO;

import com.cms.cdl.mediclaim.mediclaim_service.service.impl.PolicyHighlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mediclaim")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MediclaimController {

    private final PolicyHighlightService policyHighlightService;

    @GetMapping("/policy-highlights")
    public PolicyHighlightResponseDTO getPolicyHighlights() {
        return policyHighlightService.getActivePolicy();
    }
}
