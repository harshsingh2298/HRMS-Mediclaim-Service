package com.cms.cdl.mediclaim.mediclaim_service.repository;

import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimDependentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediclaimDependentRepository
        extends JpaRepository<MediclaimDependentEntity, String> {

    List<MediclaimDependentEntity> findByEnrollment_Id(String enrollmentId);
    List<MediclaimDependentEntity> findByEnrollmentId(String enrollmentId);


}

