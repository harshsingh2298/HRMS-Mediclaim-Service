package com.cms.cdl.mediclaim.mediclaim_service.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentResponseDTO {

    private String enrollmentId;

    // EMPLOYEE DETAILS
    private String empCode;
    private String employeeName;
    private String department;
    private String email;
    private String mobile;

    // ENROLLMENT
    private String status;
    private String optChoice;
    private Integer sumInsured;
    private BigDecimal premiumEstimate;

    // DEPENDENTS
    private List<DependentInfoDTO> dependents;

    private String notes;
}
