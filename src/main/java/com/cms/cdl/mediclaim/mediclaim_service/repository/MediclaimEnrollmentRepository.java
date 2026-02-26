package com.cms.cdl.mediclaim.mediclaim_service.repository;

import com.cms.cdl.mediclaim.mediclaim_service.entity.EnrollmentStatus;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimEnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediclaimEnrollmentRepository
        extends JpaRepository<MediclaimEnrollmentEntity, String> {
    List<MediclaimEnrollmentEntity> findByStatus(EnrollmentStatus status);

    Optional<MediclaimEnrollmentEntity>
    findTopByEmpCodeOrderByCreatedAtDesc(String empCode);
}
