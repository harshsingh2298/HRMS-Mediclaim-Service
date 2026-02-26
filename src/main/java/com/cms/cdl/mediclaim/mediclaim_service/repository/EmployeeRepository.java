package com.cms.cdl.mediclaim.mediclaim_service.repository;



import com.cms.cdl.mediclaim.mediclaim_service.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, String> {
    Optional<EmployeeEntity> findByEmpCode(String empCode);
}


