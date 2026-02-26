package com.cms.cdl.mediclaim.mediclaim_service.service.impl;

import com.cms.cdl.mediclaim.mediclaim_service.dto.DependentInfoDTO;
import com.cms.cdl.mediclaim.mediclaim_service.dto.PremiumEstimateRequestDTO;
import com.cms.cdl.mediclaim.mediclaim_service.entity.MediclaimEnrollmentEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
public class PremiumCalculationServiceImpl
        implements PremiumCalculationService {

    @Override
    public BigDecimal calculate(PremiumEstimateRequestDTO req) {

        BigDecimal total = basePremium(req.getSumInsured());

        if (req.getDependents() != null) {
            for (DependentInfoDTO d : req.getDependents()) {
                total = total.add(dependentPremium(d));
            }
        }

        return total;
    }






    // ---------------- HELPERS ----------------

    private BigDecimal basePremium(Integer sum) {

        return switch (sum) {
            case 300000 -> BigDecimal.valueOf(3500);
            case 500000 -> BigDecimal.valueOf(5200);
            case 700000 -> BigDecimal.valueOf(7800);
            default -> throw new RuntimeException("Invalid sum insured");
        };
    }

    private BigDecimal ageLoading(int age) {

        if (age <= 35) return BigDecimal.ZERO;
        if (age <= 45) return BigDecimal.valueOf(800);
        if (age <= 55) return BigDecimal.valueOf(1600);

        return BigDecimal.valueOf(3000);
    }

    private BigDecimal dependentPremium(DependentInfoDTO d) {

        int age =
                Period.between(d.getDob(), LocalDate.now()).getYears();

        return switch (d.getRelation()) {

            case SPOUSE -> BigDecimal.valueOf(2000);

            case CHILD -> BigDecimal.valueOf(1000);

            case MOTHER, FATHER,
                    IN_LAW_MOTHER, IN_LAW_FATHER -> {

                if (age <= 60) yield BigDecimal.valueOf(4000);
                if (age <= 75) yield BigDecimal.valueOf(7000);
                yield BigDecimal.valueOf(12000);
            }

            default -> BigDecimal.ZERO;
        };
    }


}

