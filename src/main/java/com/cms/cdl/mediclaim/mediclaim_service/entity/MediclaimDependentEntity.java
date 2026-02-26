package com.cms.cdl.mediclaim.mediclaim_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "mediclaim_dependent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediclaimDependentEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private MediclaimEnrollmentEntity enrollment;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private DependentRelation relation;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(name = "is_twin_group")
    private String twinGroupId;

    @Column(name = "is_primary")
    private boolean primaryMember;
}

