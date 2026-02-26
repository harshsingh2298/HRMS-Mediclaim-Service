package com.cms.cdl.mediclaim.mediclaim_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PolicyHighlightResponseDTO {
    private String version;
    private String content;
}
