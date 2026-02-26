package com.cms.cdl.mediclaim.mediclaim_service.controller;

import com.cms.cdl.mediclaim.mediclaim_service.dto.EmployeePreviewDTO;
import com.cms.cdl.mediclaim.mediclaim_service.dto.EmployeeUploadResultDTO;
import com.cms.cdl.mediclaim.mediclaim_service.entity.EnrollmentWindowEntity;
import com.cms.cdl.mediclaim.mediclaim_service.repository.EmployeeRepository;
import com.cms.cdl.mediclaim.mediclaim_service.service.impl.EmployeeUploadService;
import com.cms.cdl.mediclaim.mediclaim_service.service.impl.EnrollmentWindowService;
import com.cms.cdl.mediclaim.mediclaim_service.service.impl.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/mediclaim")
@CrossOrigin("*")
@RequiredArgsConstructor
public class MediclaimAdminController {

    private final EmployeeUploadService uploadService;
    private final EmployeeRepository employeeRepo;
    private final EnrollmentWindowService windowService;
    private final MailService mailService;

    @PostMapping("/upload-employees")
    public EmployeeUploadResultDTO upload(@RequestParam MultipartFile file) {
        return uploadService.upload(file);
    }

    @GetMapping("/employees")
    public List<EmployeePreviewDTO> employees() {
        return employeeRepo.findAll().stream().map(e -> {
            EmployeePreviewDTO d = new EmployeePreviewDTO();
            d.setEmpCode(e.getEmpCode());
            d.setFullName(e.getFullName());
            d.setEmail(e.getEmail());
            d.setDepartment(e.getDepartment());
            d.setDesignation(e.getDesignation());
            return d;
        }).toList();
    }

    @PostMapping("/send-mail")
    public void sendMail() {
        mailService.sendEnrollmentMails(employeeRepo.findAll());
    }

    @PostMapping("/enrollment-window")
    public ResponseEntity<Void> setWindow(@RequestBody EnrollmentWindowEntity window) {
        windowService.saveWindow(window);
        return ResponseEntity.ok().build();
    }
}
