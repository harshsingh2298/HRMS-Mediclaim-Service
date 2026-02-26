package com.cms.cdl.mediclaim.mediclaim_service.service.impl;

import com.cms.cdl.mediclaim.mediclaim_service.dto.EmployeeUploadResultDTO;
import com.cms.cdl.mediclaim.mediclaim_service.entity.EmployeeEntity;
import com.cms.cdl.mediclaim.mediclaim_service.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeUploadService {

    private final EmployeeRepository repository;

    public EmployeeUploadResultDTO upload(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int success = 0;
        int total = 0;

        try (Workbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                total++;

                try {
                    EmployeeEntity emp = map(row);
                    repository.save(emp);
                    success++;
                } catch (Exception e) {
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid Excel file", e);
        }

        return new EmployeeUploadResultDTO(
                total,
                success,
                errors.size(),
                errors
        );
    }

    private EmployeeEntity map(Row r) {
        return EmployeeEntity.builder()
                .empCode(cell(r, 0))
                .firstName(cell(r, 1))
                .lastName(cell(r, 2))
                .fullName(cell(r, 3))
                .email(cell(r, 4))
                .mobileNumber(cell(r, 5))
                .department(cell(r, 6))
                .designation(cell(r, 7))
                .managerEmpCode(cell(r, 8))
                .build();
    }

    private String cell(Row row, int index) {
        Cell c = row.getCell(index);
        if (c == null) return null;
        c.setCellType(CellType.STRING);
        return c.getStringCellValue().trim();
    }
}
