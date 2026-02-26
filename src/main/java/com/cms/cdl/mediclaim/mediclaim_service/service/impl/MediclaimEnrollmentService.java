package com.cms.cdl.mediclaim.mediclaim_service.service.impl;


import com.cms.cdl.mediclaim.mediclaim_service.dto.EnrollmentResponseDTO;
import com.cms.cdl.mediclaim.mediclaim_service.dto.EnrollmentSaveDTO;
import com.cms.cdl.mediclaim.mediclaim_service.dto.EnrollmentSubmitDTO;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimEnrollmentEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MediclaimEnrollmentService {

    EnrollmentResponseDTO getOrCreateDraft(String empCode);


    Page<EnrollmentResponseDTO> getAll(int page, int size);
    void optOut(String empCode);


    void approveAllSubmitted(String hrEmpCode);

    MediclaimEnrollmentEntity saveDraft(
            String empCode,
            MediclaimEnrollmentEntity draft);

    void submit(String enrollmentId, EnrollmentSubmitDTO dto);

   List <EnrollmentResponseDTO> getAll();

    void approve(String enrollmentId, String hrEmpCode);

    void reject(String enrollmentId, String hrEmpCode, String comment);

    EnrollmentResponseDTO saveDraftDTO(String empCode, EnrollmentSaveDTO dto);

    EnrollmentResponseDTO updateDraftDTO(String enrollmentId, String empCode, EnrollmentSaveDTO dto);

}
