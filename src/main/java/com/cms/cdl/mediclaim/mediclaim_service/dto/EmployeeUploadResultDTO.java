package com.cms.cdl.mediclaim.mediclaim_service.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUploadResultDTO {
    private int totalRows;
    private int successCount;
    private int failureCount;
    private List<String> errors;
}



