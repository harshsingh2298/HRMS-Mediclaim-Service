package com.cms.cdl.mediclaim.mediclaim_service.repository;

import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimPolicyHighlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediclaimPolicyHighlightRepository
        extends JpaRepository<MediclaimPolicyHighlightEntity, Long> {

    Optional<MediclaimPolicyHighlightEntity> findByActiveTrue();
}

