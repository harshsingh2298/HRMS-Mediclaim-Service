package com.cms.cdl.mediclaim.mediclaim_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EmployeeMaster {

    private String empCode;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String department;
    private String designation;
    private String mobileNumber;
    private String managerEmpCode;
}
