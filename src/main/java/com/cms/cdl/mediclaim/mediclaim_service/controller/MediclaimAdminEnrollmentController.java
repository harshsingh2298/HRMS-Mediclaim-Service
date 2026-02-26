package com.cms.cdl.mediclaim.mediclaim_service.controller;

import com.cms.cdl.mediclaim.mediclaim_service.dto.EnrollmentDecisionDTO;
import com.cms.cdl.mediclaim.mediclaim_service.service.impl.MediclaimEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/mediclaim/admin")
@RequiredArgsConstructor
public class MediclaimAdminEnrollmentController {

    private final MediclaimEnrollmentService service;

    @PostMapping("/enrollment/{id}/approve")
    public void approve(@PathVariable String id,
                        @RequestHeader("X-HR-CODE") String hrCode) {
        service.approve(id, hrCode);
    }

    @PostMapping("/enrollment/{id}/reject")
    public void reject(@PathVariable String id,
                       @RequestHeader("X-HR-CODE") String hrCode,
                       @RequestBody EnrollmentDecisionDTO dto) {
        service.reject(id, hrCode, dto.getComment());
    }
}
