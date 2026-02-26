package com.cms.cdl.mediclaim.mediclaim_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentSaveDTO {

    // only fields needed for saving draft
    private Integer sumInsured;     // 300000 / 500000 / 700000
    private String notes;           // optional
}
