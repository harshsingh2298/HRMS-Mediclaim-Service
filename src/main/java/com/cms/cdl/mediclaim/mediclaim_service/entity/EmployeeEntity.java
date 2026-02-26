package com.cms.cdl.mediclaim.mediclaim_service.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEntity extends BaseAuditEntity {

    @Id
    @Column(name = "emp_code")
    private String empCode;

    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String department;
    private String designation;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "manager_emp_code")
    private String managerEmpCode;
}


