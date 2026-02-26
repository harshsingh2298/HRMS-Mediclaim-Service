package com.cms.cdl.mediclaim.mediclaim_service.dto;

import com.cms.cdl.mediclaim.mediclaim_service.entity.DependentRelation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DependentRequestDTO {

    private String enrollmentId;

    private String name;

    private DependentRelation relation;

    private LocalDate dob;

    private Boolean primaryMember;
}

