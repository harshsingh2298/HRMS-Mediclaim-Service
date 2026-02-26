package com.cms.cdl.mediclaim.mediclaim_service.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mediclaim_policy_highlights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediclaimPolicyHighlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean active;
}
