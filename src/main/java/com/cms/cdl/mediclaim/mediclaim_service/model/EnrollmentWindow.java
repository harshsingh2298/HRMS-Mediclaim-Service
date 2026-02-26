package com.cms.cdl.mediclaim.mediclaim_service.model;

import com.cms.cdl.mediclaim.mediclaim_service.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentWindow {
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private boolean extendable;
}


