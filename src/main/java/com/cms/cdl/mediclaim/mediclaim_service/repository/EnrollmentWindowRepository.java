package com.cms.cdl.mediclaim.mediclaim_service.repository;


import com.cms.cdl.mediclaim.mediclaim_service.entity.EnrollmentWindowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentWindowRepository
        extends JpaRepository<EnrollmentWindowEntity, Long> {
}



