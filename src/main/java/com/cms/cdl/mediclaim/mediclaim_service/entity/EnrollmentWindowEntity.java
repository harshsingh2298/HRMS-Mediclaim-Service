package com.cms.cdl.mediclaim.mediclaim_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@Table(name = "enrollment_window")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentWindowEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean extendable;
}

