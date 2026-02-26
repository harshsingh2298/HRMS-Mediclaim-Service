package com.cms.cdl.mediclaim.mediclaim_service.service.impl;

import com.cms.cdl.mediclaim.mediclaim_service.dto.DependentInfoDTO;
import com.cms.cdl.mediclaim.mediclaim_service.dto.DependentRequestDTO;

import java.util.List;

public interface MediclaimDependentService {

    void addDependent(DependentRequestDTO dto);

    void removeDependent(String dependentId);

    List<DependentInfoDTO> getDependents(String enrollmentId);
}
