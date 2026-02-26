package com.cms.cdl.mediclaim.mediclaim_service.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "mediclaim_enrollment",
        indexes = {
                @Index(name = "idx_enrollment_emp", columnList = "emp_code"),
                @Index(name = "idx_enrollment_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediclaimEnrollmentEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "emp_code", nullable = false)
    private String empCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "opt_choice", nullable = false)
    private OptChoice optChoice;

    @Column(name = "sum_insured", nullable = false)
    private Integer sumInsured;

    @Column(name = "premium_estimate", precision = 12, scale = 2)
    private BigDecimal premiumEstimate;

    @Column(name = "policy_start")
    private LocalDate policyStart;

    @Column(name = "policy_end")
    private LocalDate policyEnd;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;


    @Column(name = "created_by")
    private String createdBy;

    private String updatedBy;


    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediclaimDependentEntity> dependents;
}

