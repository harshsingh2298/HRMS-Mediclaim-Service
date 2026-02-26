package com.cms.cdl.mediclaim.mediclaim_service.service.impl;



import com.cms.cdl.mediclaim.mediclaim_service.entity.DependentRelation;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimDependentEntity;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimEnrollmentEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class DependentValidationService {

    public void validateDependent(
            MediclaimEnrollmentEntity enrollment,
            MediclaimDependentEntity incoming) {

        if (incoming.getDob() == null) {
            throw new RuntimeException("DOB is mandatory");
        }

        switch (incoming.getRelation()) {
            case CHILD -> validateChild(enrollment, incoming);
            case MOTHER, FATHER -> validateParents(enrollment, incoming);
            case IN_LAW_MOTHER, IN_LAW_FATHER -> validateInLaws(enrollment, incoming);
            default -> {
                // spouse, others — no special rules yet
            }
        }
    }

    // ---------------- CHILD RULES ----------------

    private void validateChild(
            MediclaimEnrollmentEntity enrollment,
            MediclaimDependentEntity incoming) {

        LocalDate policyStart = enrollment.getPolicyStart();

        int age = Period.between(incoming.getDob(), policyStart).getYears();

        if (age > 25) {
            throw new RuntimeException("Child age cannot exceed 25 years");
        }

        List<MediclaimDependentEntity> children =
                enrollment.getDependents()
                        .stream()
                        .filter(d -> d.getRelation() == DependentRelation.CHILD)
                        .toList();

        if (children.size() < 2) return;

        if (children.size() == 2) {
            boolean twinsExist = hasTwins(children);

            if (!twinsExist) {
                throw new RuntimeException(
                        "Third child allowed only if twins exist");
            }
        }

        if (children.size() >= 3) {
            throw new RuntimeException("Maximum children limit exceeded");
        }
    }

    private boolean hasTwins(List<MediclaimDependentEntity> children) {
        for (int i = 0; i < children.size(); i++) {
            for (int j = i + 1; j < children.size(); j++) {
                if (children.get(i).getDob()
                        .equals(children.get(j).getDob())) {
                    return true;
                }
            }
        }
        return false;
    }

    // ---------------- PARENT RULES ----------------

    private void validateParents(
            MediclaimEnrollmentEntity enrollment,
            MediclaimDependentEntity incoming) {

        checkParentAge(incoming, enrollment.getPolicyStart());

        boolean inLawsPresent =
                enrollment.getDependents()
                        .stream()
                        .anyMatch(d ->
                                d.getRelation() == DependentRelation.IN_LAW_MOTHER ||
                                        d.getRelation() == DependentRelation.IN_LAW_FATHER);

        if (inLawsPresent) {
            throw new RuntimeException(
                    "Cannot add parents when in-laws are already added");
        }
    }

    private void validateInLaws(
            MediclaimEnrollmentEntity enrollment,
            MediclaimDependentEntity incoming) {

        checkParentAge(incoming, enrollment.getPolicyStart());

        boolean parentsPresent =
                enrollment.getDependents()
                        .stream()
                        .anyMatch(d ->
                                d.getRelation() == DependentRelation.MOTHER ||
                                        d.getRelation() == DependentRelation.FATHER);

        if (parentsPresent) {
            throw new RuntimeException(
                    "Cannot add in-laws when parents are already added");
        }
    }

    private void checkParentAge(
            MediclaimDependentEntity d,
            LocalDate policyStart) {

        int age = Period.between(d.getDob(), policyStart).getYears();

        if (age >= 85) {
            throw new RuntimeException(
                    "Parent age must be below 85 years");
        }
    }
}
