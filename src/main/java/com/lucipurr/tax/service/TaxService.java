package com.lucipurr.tax.service;

import com.lucipurr.tax.abstractions.ITaxService;
import com.lucipurr.tax.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaxService implements ITaxService {


    @Override
    public String netTax(Employee employee) {
        double netTax;
        log.info("Employee Details:\n{}", employee);
        String regime = employee.getEmp().getRegime();
        log.info(regime);
        if (regime.equalsIgnoreCase("new")) {
            netTax = newRegimeTax(employee);
        } else {
            netTax = oldRegimeTax(employee);
        }
        return Double.toString(netTax);
    }

    private double oldRegimeTax(Employee employee) {
        double netSalary = employee.getIncome().tctc();
        double deductions = employee.getDeductions().netExemption();
        double hra = calculateHRADeduction(employee);
        double pf = employee.getIncome().getPf();
        double grossSalary = netSalary - (deductions + hra + pf);
        log.info("\nTCTC:{}\ndeductions:{}\nhra:{}\npf:{}\ngrossSalary:{}\nAll Deductions:{}", netSalary, deductions, hra, pf, grossSalary, deductions + hra + pf);
        double iTax = 0;
        if (grossSalary < 250000) return iTax;
        double slab = 500000;
        double tax = 20;
        if (grossSalary > 1000000) {
            grossSalary -= 1000000;
            grossSalary *= 30;
            grossSalary /= 100;
            iTax += grossSalary;
            grossSalary = 1000000;
        }
        iTax += calculateOldRegime(grossSalary, slab, tax);
        return iTax;
    }

    private double calculateOldRegime(double grossSalary, double slab, double tax) {
        double iTax = 0;
        while (slab != 0) {
            double difference = grossSalary - slab;
            log.info("difference:{}", difference);
            if (difference < 0) {
                slab -= 250000;
                tax = 5;
                continue;
            }
            difference *= tax;
            difference /= 100;
            iTax += difference;
            grossSalary = slab;
            slab -= 250000;
            tax = 5;
        }
        double cess = 4 * iTax;
        cess /= 100;
        iTax = iTax + cess;
        return iTax;
    }

    private double newRegimeTax(Employee employee) {
        double taxableSalary = employee.getIncome().tctc();
        double iTax = 0;
        if (taxableSalary < 250000)
            return iTax;
        double slab = 1250000;
        double tax = 25;
        if (taxableSalary > 1500000) {
            taxableSalary -= 1500000;
            taxableSalary *= 30;
            taxableSalary /= 100;
            iTax += taxableSalary;
            taxableSalary = 1500000;
        }
        iTax += calculateNewRegime(taxableSalary, slab, tax);
        double cess = 4 * iTax;
        cess /= 100;
        iTax = iTax + cess;
        return iTax;
    }

    private double calculateNewRegime(double salary, double slab, double tax) {
        double iTax = 0;
        while (slab != 0) {
            double difference = salary - slab;
            if (difference < 0) {
                slab -= 250000;
                tax -= 5;
                continue;
            }
            difference *= tax;
            difference /= 100;
            iTax += difference;
            salary = slab;
            slab -= 250000;
            tax -= 5;
        }
        return iTax;
    }

    public double calculateHRADeduction(Employee employee) {
        double rentPaid = employee.getDeductions().getRentPaid();
        String location = employee.getEmp().getLocation();
        double hra;
        if (location.equalsIgnoreCase("urban")) {
            hra = employee.getIncome().getBasicSalary() / 2;
        } else {
            hra = employee.getIncome().getBasicSalary() * 40;
            hra /= 100;
        }
        double percentileSalary = employee.getIncome().getBasicSalary() * 10;
        percentileSalary /= 100;
        rentPaid = rentPaid - percentileSalary;
        double hraReceived = employee.getIncome().getHra();
        log.info("hra:{}\nrentPaid:{}\nhraReceived:{}", hra, rentPaid, hraReceived);
        return Math.min(hraReceived, (Math.min(rentPaid, hra)));
    }
}
