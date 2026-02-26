package com.cms.cdl.mediclaim.mediclaim_service.dto;



import com.cms.cdl.mediclaim.mediclaim_service.entity.DependentRelation;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PremiumDependentDTO {
    private DependentRelation relation;
    private LocalDate dob;
}
