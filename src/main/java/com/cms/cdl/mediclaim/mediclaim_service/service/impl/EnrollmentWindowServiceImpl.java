package com.cms.cdl.mediclaim.mediclaim_service.service.impl;

import com.cms.cdl.mediclaim.mediclaim_service.entity.EnrollmentWindowEntity;
import com.cms.cdl.mediclaim.mediclaim_service.model.EnrollmentWindow;
import com.cms.cdl.mediclaim.mediclaim_service.repository.EnrollmentWindowRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentWindowServiceImpl implements EnrollmentWindowService {

    private final EnrollmentWindowRepository repository;

    @Override
    public void saveWindow(EnrollmentWindowEntity window) {

        if (window.getStartDate() == null || window.getEndDate() == null) {
            throw new RuntimeException("Start date and end date are required");
        }

        if (window.getStartDate().isAfter(window.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        // Deactivate all existing windows
        repository.findAll()
                .forEach(w -> w.setActive(false));

        repository.saveAll(repository.findAll());

        // Save new window
        EnrollmentWindowEntity newWindow =
                EnrollmentWindowEntity.builder()
                        .active(true)
                        .startDate(window.getStartDate())
                        .endDate(window.getEndDate())
                        .extendable(window.isExtendable())
                        .build();

        repository.save(newWindow);
    }


}
