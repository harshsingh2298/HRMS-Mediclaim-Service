package com.cms.cdl.mediclaim.mediclaim_service.dto;
import com.cms.cdl.mediclaim.mediclaim_service.entity.DependentRelation;
import lombok.*;

import java.time.LocalDate;


@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DependentInfoDTO {

    private String id;                 // ✅ needed for delete
    private String name;
    private int age;                   // ✅ show in UI
    private DependentRelation relation;
    private LocalDate dob;
    private Boolean primaryMember;
}

