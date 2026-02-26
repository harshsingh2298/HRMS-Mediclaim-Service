package com.cms.cdl.mediclaim.mediclaim_service.controller;

import com.cms.cdl.mediclaim.mediclaim_service.dto.DependentInfoDTO;
import com.cms.cdl.mediclaim.mediclaim_service.dto.DependentRequestDTO;
import com.cms.cdl.mediclaim.mediclaim_service.service.impl.MediclaimDependentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/mediclaim/dependents")
@RequiredArgsConstructor
public class MediclaimDependentController {

    private final MediclaimDependentService dependentService;


    @PostMapping
    public void add(@RequestBody DependentRequestDTO dto) {
        dependentService.addDependent(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        dependentService.removeDependent(id);
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public List<DependentInfoDTO> list(@PathVariable String enrollmentId) {
        return dependentService.getDependents(enrollmentId);
    }


}
