package com.cms.cdl.mediclaim.mediclaim_service.service.impl;

import com.cms.cdl.mediclaim.mediclaim_service.dto.DependentInfoDTO;
import com.cms.cdl.mediclaim.mediclaim_service.dto.DependentRequestDTO;
import com.cms.cdl.mediclaim.mediclaim_service.entity.DependentRelation;
import com.cms.cdl.mediclaim.mediclaim_service.entity.EnrollmentStatus;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimDependentEntity;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimEnrollmentEntity;
import com.cms.cdl.mediclaim.mediclaim_service.repository.MediclaimDependentRepository;
import com.cms.cdl.mediclaim.mediclaim_service.repository.MediclaimEnrollmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MediclaimDependentServiceImpl implements MediclaimDependentService {

    private final MediclaimEnrollmentRepository enrollmentRepo;
    private final MediclaimDependentRepository dependentRepo;

    @Override
    public void addDependent(DependentRequestDTO dto) {

        // ✅ Fix 1: prevent null enrollmentId crash
        if (dto.getEnrollmentId() == null || dto.getEnrollmentId().isBlank()) {
            throw new RuntimeException("EnrollmentId is required");
        }

        if (dto.getDob() == null) {
            throw new RuntimeException("DOB is required");
        }

        if (dto.getRelation() == null) {
            throw new RuntimeException("Relation is required");
        }

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new RuntimeException("Dependent name is required");
        }

        MediclaimEnrollmentEntity enrollment =
                enrollmentRepo.findById(dto.getEnrollmentId())
                        .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // ❌ OPT_OUT protection
        if (enrollment.getStatus() == EnrollmentStatus.OPT_OUT) {
            throw new RuntimeException("Cannot add dependents after opt-out");
        }

        // ❌ Only DRAFT allowed
        if (enrollment.getStatus() != EnrollmentStatus.DRAFT) {
            throw new RuntimeException("Dependents allowed only in draft stage");
        }

        // ✅ Always use repo to get latest dependents
        List<MediclaimDependentEntity> existing =
                dependentRepo.findByEnrollmentId(dto.getEnrollmentId());

        // ---------------- RULE 1: CHILD AGE ≤ 25 ----------------
        if (dto.getRelation() == DependentRelation.CHILD) {

            int age = Period.between(dto.getDob(), LocalDate.now()).getYears();
            if (age > 25) {
                throw new RuntimeException("Child age cannot exceed 25 years");
            }

            long childCount = existing.stream()
                    .filter(d -> d.getRelation() == DependentRelation.CHILD)
                    .count();

            // RULE 2: THIRD CHILD ONLY IF TWINS
            if (childCount >= 2) {

                Map<LocalDate, Long> dobCount =
                        existing.stream()
                                .filter(d -> d.getRelation() == DependentRelation.CHILD)
                                .collect(Collectors.groupingBy(
                                        MediclaimDependentEntity::getDob,
                                        Collectors.counting()
                                ));

                boolean twinsExist = dobCount.values().stream().anyMatch(c -> c >= 2);

                if (!twinsExist) {
                    throw new RuntimeException("Third child allowed only in case of twins");
                }
            }
        }

        // ---------------- RULE 3: Parents vs In-laws ----------------
        if (isParentOrInLaw(dto.getRelation())) {

            boolean parentExists = existing.stream().anyMatch(d -> isParentRelation(d.getRelation()));
            boolean inLawExists = existing.stream().anyMatch(d -> isInLawRelation(d.getRelation()));

            boolean addingParent = isParentRelation(dto.getRelation());
            boolean addingInLaw = isInLawRelation(dto.getRelation());

            if (addingParent && inLawExists) {
                throw new RuntimeException("Cannot add parents when in-laws exist");
            }
            if (addingInLaw && parentExists) {
                throw new RuntimeException("Cannot add in-laws when parents exist");
            }

            // ---------------- RULE 4: Parent/InLaw age ≤ 85 ----------------
            int age = Period.between(dto.getDob(), LocalDate.now()).getYears();
            if (age > 85) {
                throw new RuntimeException("Parent age cannot exceed 85 years");
            }
        }

        MediclaimDependentEntity dependent =
                MediclaimDependentEntity.builder()
                        .enrollment(enrollment)
                        .name(dto.getName().trim())
                        .relation(dto.getRelation())
                        .dob(dto.getDob())
                        .primaryMember(Boolean.TRUE.equals(dto.getPrimaryMember()))
                        .build();

        dependentRepo.save(dependent);
    }

    private int calculateAge(LocalDate dob) {
        if (dob == null) return 0;

        LocalDate today = LocalDate.now();
        int age = today.getYear() - dob.getYear();

        if (today.getDayOfYear() < dob.getDayOfYear()) {
            age--;
        }
        return age;
    }


    @Override
    public void removeDependent(String dependentId) {
        if (dependentId == null || dependentId.isBlank()) {
            throw new RuntimeException("DependentId is required");
        }
        dependentRepo.deleteById(dependentId);
    }

    @Override
    public List<DependentInfoDTO> getDependents(String enrollmentId) {

        if (enrollmentId == null || enrollmentId.isBlank()) {
            throw new RuntimeException("EnrollmentId is required");
        }

        enrollmentRepo.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        List<MediclaimDependentEntity> list =
                dependentRepo.findByEnrollmentId(enrollmentId);

        return list.stream().map(d -> {

            DependentInfoDTO dto = new DependentInfoDTO();
            dto.setId(d.getId());
            dto.setName(d.getName());
            dto.setRelation(d.getRelation());
            dto.setDob(d.getDob());
            dto.setAge(calculateAge(d.getDob())); // ✅ FIXED
            dto.setPrimaryMember(Boolean.TRUE.equals(d.isPrimaryMember()));

            return dto;

        }).toList();
    }


    private boolean isParentOrInLaw(DependentRelation r) {
        return isParentRelation(r) || isInLawRelation(r);
    }

    private boolean isParentRelation(DependentRelation r) {
        return r == DependentRelation.MOTHER || r == DependentRelation.FATHER;
    }

    private boolean isInLawRelation(DependentRelation r) {
        return r == DependentRelation.IN_LAW_MOTHER || r == DependentRelation.IN_LAW_FATHER;
    }
}
