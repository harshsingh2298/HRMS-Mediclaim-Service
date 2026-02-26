package com.cms.cdl.mediclaim.mediclaim_service.service.impl;

import com.cms.cdl.mediclaim.mediclaim_service.dto.*;
import com.cms.cdl.mediclaim.mediclaim_service.entity.*;
import com.cms.cdl.mediclaim.mediclaim_service.repository.EmployeeRepository;
import com.cms.cdl.mediclaim.mediclaim_service.repository.EnrollmentWindowRepository;
import com.cms.cdl.mediclaim.mediclaim_service.repository.MediclaimEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MediclaimEnrollmentServiceImpl implements MediclaimEnrollmentService {

    private final MediclaimEnrollmentRepository enrollmentRepo;
    private final EnrollmentWindowRepository windowRepo;
    private final PremiumCalculationService premiumService;
    private final MailService mailService;
    private final EmployeeRepository employeeRepo;

    // ---------------- USER ----------------

    @Override
    public EnrollmentResponseDTO getOrCreateDraft(String empCode) {

        MediclaimEnrollmentEntity e =
                enrollmentRepo.findTopByEmpCodeOrderByCreatedAtDesc(empCode)
                        .orElseGet(() -> createDraft(empCode));

        return toResponse(e);
    }

    @Override
    public Page<EnrollmentResponseDTO> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return enrollmentRepo.findAll(pageable)
                .map(this::toResponse);
    }
    private boolean isDependentOverAge(MediclaimDependentEntity d,
                                       LocalDate policyStart) {

        int age = Period.between(d.getDob(), policyStart).getYears();

        if (d.getRelation() == DependentRelation.CHILD && age > 25) return true;

        if ((d.getRelation() == DependentRelation.FATHER ||
                d.getRelation() == DependentRelation.MOTHER ||
                d.getRelation() == DependentRelation.IN_LAW_FATHER ||
                d.getRelation() == DependentRelation.IN_LAW_MOTHER) && age > 85)
            return true;

        return false;
    }

    private int calculateAge(LocalDate dob) {
        if (dob == null) return 0;
        return Period.between(dob, LocalDate.now()).getYears();
    }

    private MediclaimEnrollmentEntity createDraft(String empCode) {

        MediclaimEnrollmentEntity entity = MediclaimEnrollmentEntity.builder()
                .empCode(empCode)
                .status(EnrollmentStatus.DRAFT)
                .optChoice(OptChoice.IN)
                .sumInsured(300000) // default
                .policyStart(LocalDate.now())
                .policyEnd(LocalDate.now().plusYears(1))
                .createdOn(LocalDateTime.now())
                .createdBy(empCode)
                .build();

        return enrollmentRepo.save(entity);
    }

    @Override
    public EnrollmentResponseDTO saveDraftDTO(String empCode, EnrollmentSaveDTO dto) {

        // ✅ window validation
        validateWindow();

        MediclaimEnrollmentEntity e =
                enrollmentRepo.findTopByEmpCodeOrderByCreatedAtDesc(empCode)
                        .orElseGet(() -> createDraft(empCode));

        ensureStatus(e, EnrollmentStatus.DRAFT);

        if (dto.getSumInsured() != null) {
            e.setSumInsured(dto.getSumInsured());
        }
        e.setNotes(dto.getNotes());
        e.setUpdatedAt(LocalDateTime.now());

        enrollmentRepo.save(e);

        return toResponse(e);
    }

    @Override
    public EnrollmentResponseDTO updateDraftDTO(String enrollmentId, String empCode, EnrollmentSaveDTO dto) {

        validateWindow();

        MediclaimEnrollmentEntity e = getEnrollment(enrollmentId);

        // ✅ ensure employee can only update their own enrollment
        if (!e.getEmpCode().equals(empCode)) {
            throw new RuntimeException("Unauthorized enrollment update attempt");
        }

        ensureStatus(e, EnrollmentStatus.DRAFT);

        if (dto.getSumInsured() != null) {
            e.setSumInsured(dto.getSumInsured());
        }
        e.setNotes(dto.getNotes());
        e.setUpdatedAt(LocalDateTime.now());

        enrollmentRepo.save(e);

        return toResponse(e);
    }

    @Override
    public void submit(String enrollmentId, EnrollmentSubmitDTO dto) {

        validateWindow();

        MediclaimEnrollmentEntity e = getEnrollment(enrollmentId);

        ensureStatus(e, EnrollmentStatus.DRAFT);

        // update required submit fields
        e.setSumInsured(dto.getSumInsured());
        e.setStatus(EnrollmentStatus.SUBMITTED);
        e.setSubmittedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());

        // ✅ premium calc
        PremiumEstimateRequestDTO req = mapToPremiumRequest(e);
        BigDecimal premium = premiumService.calculate(req);

        e.setPremiumEstimate(premium);

        enrollmentRepo.save(e);

        // ✅ mail to employee
        mailService.sendEnrollmentSubmittedMail(e);
    }

    @Override
    public List<EnrollmentResponseDTO> getAll() {
        return enrollmentRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void optOut(String empCode) {

        validateWindow();

        MediclaimEnrollmentEntity e =
                enrollmentRepo.findTopByEmpCodeOrderByCreatedAtDesc(empCode)
                        .orElseGet(() -> createDraft(empCode));

        ensureStatus(e, EnrollmentStatus.DRAFT);

        e.setOptChoice(OptChoice.OUT);
        e.setStatus(EnrollmentStatus.OPT_OUT);
        e.setSubmittedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());

        enrollmentRepo.save(e);

        mailService.sendOptOutMail(e);
    }

    @Override
    public MediclaimEnrollmentEntity saveDraft(String empCode, MediclaimEnrollmentEntity draft) {

        validateWindow();

        // empCode always belongs to logged-in employee
        draft.setEmpCode(empCode);

        // always draft
        draft.setStatus(EnrollmentStatus.DRAFT);
        draft.setOptChoice(OptChoice.IN);

        if (draft.getCreatedAt() == null) {
            draft.setCreatedAt(LocalDateTime.now());
            draft.setCreatedBy(empCode);
        }

        draft.setUpdatedAt(LocalDateTime.now());

        // default values safety
        if (draft.getSumInsured() == null) {
            draft.setSumInsured(300000);
        }
        if (draft.getPolicyStart() == null) {
            draft.setPolicyStart(LocalDate.now());
        }
        if (draft.getPolicyEnd() == null) {
            draft.setPolicyEnd(LocalDate.now().plusYears(1));
        }

        return enrollmentRepo.save(draft);
    }

    // ---------------- HR ----------------

    @Override
    public void approve(String enrollmentId, String hrEmpCode) {

        MediclaimEnrollmentEntity e = getEnrollment(enrollmentId);
        ensureStatus(e, EnrollmentStatus.SUBMITTED);

        List<MediclaimDependentEntity> invalid =
                e.getDependents().stream()
                        .filter(d -> isDependentOverAge(d, e.getPolicyStart()))
                        .toList();

        // Remove invalid dependents
        if (!invalid.isEmpty()) {
            e.getDependents().removeAll(invalid);
        }

        // Recalculate premium
        PremiumEstimateRequestDTO req = new PremiumEstimateRequestDTO();
        req.setSumInsured(e.getSumInsured());

        List<DependentInfoDTO> validDeps =
                e.getDependents().stream()
                        .map(d -> new DependentInfoDTO(
                                d.getId(),
                                d.getName(),
                                calculateAge(d.getDob()),
                                d.getRelation(),
                                d.getDob(),
                                d.isPrimaryMember()
                        ))
                        .toList();

        req.setDependents(validDeps);

        BigDecimal newPremium = premiumService.calculate(req);
        e.setPremiumEstimate(newPremium);

        e.setStatus(EnrollmentStatus.APPROVED);
        e.setUpdatedAt(LocalDateTime.now());

        enrollmentRepo.save(e);
        EmployeeEntity hr = employeeRepo.findByEmpCode(hrEmpCode)
                .orElseThrow(() -> new RuntimeException("Invalid HR code"));

        if (!"HR".equalsIgnoreCase(hr.getDepartment())) {
            throw new RuntimeException("Unauthorized action. Only HR allowed.");
        }
        if (invalid.isEmpty()) {
            mailService.sendApprovedMail(e);
        } else {
            mailService.sendDependentAgeExceededMail(e, invalid);
        }
    }


    @Override
    public void reject(String enrollmentId, String hrEmpCode, String comment) {

        MediclaimEnrollmentEntity e = getEnrollment(enrollmentId);

        ensureStatus(e, EnrollmentStatus.SUBMITTED);

        e.setStatus(EnrollmentStatus.REJECTED);
        e.setNotes(comment);
        e.setUpdatedAt(LocalDateTime.now());

        enrollmentRepo.save(e);

        mailService.sendRejectedMail(e);
    }

    // ---------------- HELPERS ----------------

    private MediclaimEnrollmentEntity getEnrollment(String id) {
        return enrollmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
    }

    private void validateWindow() {

        EnrollmentWindowEntity w =
                windowRepo.findAll().stream()
                        .filter(EnrollmentWindowEntity::isActive)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Enrollment window not active"));

        LocalDate today = LocalDate.now();
        if (today.isBefore(w.getStartDate()) || today.isAfter(w.getEndDate())) {
            throw new RuntimeException("Outside enrollment window");
        }
    }

    private void ensureStatus(MediclaimEnrollmentEntity e, EnrollmentStatus expected) {
        if (e.getStatus() != expected) {
            throw new RuntimeException("Invalid state: " + e.getStatus());
        }
    }

    private EnrollmentResponseDTO toResponse(MediclaimEnrollmentEntity e) {

        EmployeeEntity emp = employeeRepo.findByEmpCode(e.getEmpCode())
                .orElse(null);

        List<DependentInfoDTO> deps =
                e.getDependents() == null
                        ? List.<DependentInfoDTO>of()
                        : e.getDependents().stream()
                        .map(d -> DependentInfoDTO.builder()
                                .name(d.getName())
                                .relation(d.getRelation())
                                .age(calculateAge(d.getDob()))
                                .build()
                        ).toList();

        return EnrollmentResponseDTO.builder()
                .enrollmentId(e.getId())
                .empCode(e.getEmpCode())
                .employeeName(emp != null ? emp.getFullName() : "-")
                .department(emp != null ? emp.getDepartment() : "-")
                .email(emp != null ? emp.getEmail() : "-")
                .mobile(emp != null ? emp.getMobileNumber() : "-")
                .status(e.getStatus().name())
                .optChoice(e.getOptChoice().name())
                .sumInsured(e.getSumInsured())
                .premiumEstimate(e.getPremiumEstimate())
                .dependents(deps)
                .notes(e.getNotes())
                .build();
    }



    private PremiumEstimateRequestDTO mapToPremiumRequest(MediclaimEnrollmentEntity e) {

        PremiumEstimateRequestDTO req = new PremiumEstimateRequestDTO();
        req.setSumInsured(e.getSumInsured());
        req.setEmployeeDob(null); // if you have employee DOB later set it
        req.setDependents(null);  // dependents service will fill later, now keep null
        return req;
    }


    @Override
    public void approveAllSubmitted(String hrEmpCode) {

        List<MediclaimEnrollmentEntity> list =
                enrollmentRepo.findByStatus(EnrollmentStatus.SUBMITTED);

//        for (MediclaimEnrollmentEntity e : list) {
//            e.setStatus(EnrollmentStatus.APPROVED);
//            e.setUpdatedAt(LocalDateTime.now());
//            enrollmentRepo.save(e);
//
//            mailService.sendApprovedMail(e);
//        }
        for (MediclaimEnrollmentEntity e : list) {
            try {
                approve(e.getId(), hrEmpCode);
            } catch (Exception ex) {
                // log and continue
            }
        }
    }

}
