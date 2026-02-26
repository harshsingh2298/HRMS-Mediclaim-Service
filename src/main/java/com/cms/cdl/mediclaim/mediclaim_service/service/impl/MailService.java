package com.cms.cdl.mediclaim.mediclaim_service.service.impl;

import com.cms.cdl.mediclaim.mediclaim_service.dto.DependentInfoDTO;
import com.cms.cdl.mediclaim.mediclaim_service.entity.EmployeeEntity;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimDependentEntity;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimEnrollmentEntity;
import com.cms.cdl.mediclaim.mediclaim_service.repository.EmployeeRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmployeeRepository employeeRepo; // ✅ ADD

    private void send(String toEmail,
                      String subject,
                      String html) {

        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(msg, true, "UTF-8");

            helper.setFrom("cdladmin@cms.co.in");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(msg);

        } catch (Exception ex) {
            throw new RuntimeException("Mail failed", ex);
        }
    }

    public void sendEnrollmentMails(List<EmployeeEntity> employees) {
        for (EmployeeEntity e : employees) {

            Context ctx = new Context();
            ctx.setVariable("name", e.getFullName());

            String html = templateEngine.process("mails/mediclaim-open", ctx);

            send(e.getEmail(),
                    "Mediclaim Enrollment Window Open",
                    html);
        }
    }


    public void sendEnrollmentSubmittedMail(MediclaimEnrollmentEntity e) {

        EmployeeEntity emp = employeeRepo.findByEmpCode(e.getEmpCode())
                .orElseThrow();

        Context ctx = new Context();
        ctx.setVariable("name", emp.getFullName());
        ctx.setVariable("premium", e.getPremiumEstimate());

        List<DependentInfoDTO> dependents = e.getDependents() == null
                ? List.of()
                : e.getDependents().stream()
                .map(d -> new DependentInfoDTO(
                        d.getId(),
                        d.getName(),
                        Period.between(d.getDob(), LocalDate.now()).getYears(),
                        d.getRelation(),
                        d.getDob(),
                        d.isPrimaryMember()
                ))
                .toList();

        ctx.setVariable("dependents", dependents);

        String html = templateEngine.process("mails/mediclaim-submitted", ctx);

        send(emp.getEmail(),
                "Mediclaim Enrollment Submitted Successfully",
                html);
    }



    public void sendOptOutMail(MediclaimEnrollmentEntity e) {
        send(
                resolveEmail(e.getEmpCode()),
                "Mediclaim Opt-Out Confirmed",
                "You have opted out of Mediclaim for this policy year."
        );
    }

    public void sendApprovedMail(MediclaimEnrollmentEntity e) {
        EmployeeEntity emp = employeeRepo.findByEmpCode(e.getEmpCode())
                .orElseThrow();

        Context ctx = new Context();
        ctx.setVariable("name", emp.getFullName());

        String html = templateEngine.process("mails/mediclaim-approved", ctx);

        send(emp.getEmail(),
                "Mediclaim Enrollment Approved",
                html);
    }

    public void sendRejectedMail(MediclaimEnrollmentEntity e) {
        EmployeeEntity emp = employeeRepo.findByEmpCode(e.getEmpCode())
                .orElseThrow();

        Context ctx = new Context();
        ctx.setVariable("name", emp.getFullName());
        ctx.setVariable("reason", e.getNotes());

        String html = templateEngine.process("mails/mediclaim-rejected", ctx);

        send(emp.getEmail(),
                "Mediclaim Enrollment Rejected",
                html);
    }

    public void sendDependentAgeExceededMail(MediclaimEnrollmentEntity e,
                                             List<MediclaimDependentEntity> invalidDependents) {

        EmployeeEntity emp = employeeRepo.findByEmpCode(e.getEmpCode())
                .orElseThrow();

        Context ctx = new Context();
        ctx.setVariable("name", emp.getFullName());
        ctx.setVariable("premium", e.getPremiumEstimate());

        List<Map<String, Object>> deps = invalidDependents.stream()
                .map(d -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("name", d.getName());
                    m.put("relation", d.getRelation().name());
                    m.put("age", Period.between(d.getDob(), LocalDate.now()).getYears());
                    return m;
                })
                .toList();

        ctx.setVariable("invalidDependents", deps);

        String html = templateEngine.process("mails/mediclaim-age-exceeded", ctx);

        send(emp.getEmail(),
                "Important: Dependent Age Limit Exceeded – Mediclaim Update",
                html);
    }



    private String resolveEmail(String empCode) {
        return employeeRepo.findByEmpCode(empCode)
                .map(EmployeeEntity::getEmail)
                .orElseThrow(() -> new RuntimeException("Email not found for empCode: " + empCode));
    }

}
