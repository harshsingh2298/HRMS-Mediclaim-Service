package com.cms.cdl.mediclaim.mediclaim_service.controller;

import com.cms.cdl.mediclaim.mediclaim_service.dto.EnrollmentResponseDTO;
import com.cms.cdl.mediclaim.mediclaim_service.dto.EnrollmentSubmitDTO;
import com.cms.cdl.mediclaim.mediclaim_service.dto.EnrollmentSaveDTO;
import com.cms.cdl.mediclaim.mediclaim_service.service.impl.MediclaimEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mediclaim")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MediclaimEnrollmentController {

    private final MediclaimEnrollmentService enrollmentService;

    // ✅ Get draft (or create) for employee
    @GetMapping("/enrollment")
    public EnrollmentResponseDTO getEnrollment(@RequestParam String empCode) {
        return enrollmentService.getOrCreateDraft(empCode);
    }


    @GetMapping("/get-all/enrollments")
    public Page<EnrollmentResponseDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return enrollmentService.getAll(page, size);
    }
    // ✅ Save draft (create/update)
    @PostMapping("/enrollment")
    public EnrollmentResponseDTO saveDraft(@RequestParam String empCode,
                                           @RequestBody EnrollmentSaveDTO dto) {
        return enrollmentService.saveDraftDTO(empCode, dto);
    }

    // ✅ Update draft by id
    @PutMapping("/enrollment/{id}")
    public EnrollmentResponseDTO updateDraft(@PathVariable String id,
                                             @RequestParam String empCode,
                                             @RequestBody EnrollmentSaveDTO dto) {
        return enrollmentService.updateDraftDTO(id, empCode, dto);
    }

    // ✅ Submit enrollment
    @PostMapping("/enrollment/{id}/submit")
    public void submit(@PathVariable String id,
                       @RequestBody EnrollmentSubmitDTO dto) {
        enrollmentService.submit(id, dto);
    }

    //  Opt out
    @PostMapping("/enrollment/optout")
    public void optOut(@RequestParam String empCode) {
        enrollmentService.optOut(empCode);
    }





    //  HR Approve single enrollment
    @PostMapping("/enrollment/{id}/approve")
    public void approve(@PathVariable String id,
                        @RequestParam String hrEmpCode) {
        enrollmentService.approve(id, hrEmpCode);
    }

    // ✅ HR Reject single enrollment
    @PostMapping("/enrollment/{id}/reject")
    public void reject(@PathVariable String id,
                       @RequestParam String hrEmpCode,
                       @RequestParam String comment) {
        enrollmentService.reject(id, hrEmpCode, comment);
    }



    @PostMapping("/admin/enrollment/approve-all")
    public void approveAll(@RequestParam String hrEmpCode) {
        enrollmentService.approveAllSubmitted(hrEmpCode);
    }


}
