package com.cms.cdl.mediclaim.mediclaim_service.service.impl;

import com.cms.cdl.mediclaim.mediclaim_service.dto.PolicyHighlightResponseDTO;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimPolicyHighlightEntity;
import com.cms.cdl.mediclaim.mediclaim_service.repository.MediclaimPolicyHighlightRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyHighlightServiceImpl implements PolicyHighlightService {

    private final MediclaimPolicyHighlightRepository repository;

    @Override
    public PolicyHighlightResponseDTO getActivePolicy() {

        MediclaimPolicyHighlightEntity entity =
                repository.findByActiveTrue()
                        .orElseThrow(() ->
                                new RuntimeException("Active policy highlights not configured"));

        return PolicyHighlightResponseDTO.builder()
                .version(entity.getVersion())
                .content(entity.getContent())
                .build();
    }
}
