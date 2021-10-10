package com.lucipurr.tax.service;

import com.lucipurr.dbrepo.repository.*;
import com.lucipurr.tax.abstractions.IDataBaseService;
import com.lucipurr.tax.abstractions.ITaxService;
import com.lucipurr.tax.model.Employee;
import com.lucipurr.tax.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class TaxService implements ITaxService {

    @Autowired
    IDataBaseService IDataBaseservice;
    @Autowired
    private EmployeeInfoMasterRepository employeeInfoMasterRepository;

    public TaxService() {
    }

    @Override
    public Response calculateTax(String empId) {
        Employee employee = IDataBaseservice.fetchDetailsEmpId(empId);
        String regime = employee.getEmp().getRegime();
        long age = employee.getEmp().getAge();
        Response response = new Response();
        double netTax;
        if (regime.equalsIgnoreCase("new")) {
            netTax = Math.round(newRegimeTax(employee, response));
        } else {
            netTax = Math.round(oldRegimeTax(employee, response));
        }
        response.setSavings(Double.toString(Math.round(newRegimeTax(employee, response) - netTax)));
        response.setTotalTax(Double.toString(Math.round(netTax)));
        response.setRegime(employee.getEmp().getRegime());
        log.info("Response That i will Forward to UI.{}", response);
        return response;
    }

    private double oldRegimeTax(Employee employee, Response response) {
        double netSalary = employee.getIncome().tctc();
        double deductions = employee.getDeductions().netExemption();
        double hra = calculateHRADeduction(employee);
        double pf = employee.getIncome().getPf();
        double grossSalary = netSalary - (deductions + hra + pf);
        response.setNetSalary(Double.toString(netSalary));
        log.info("\nDeductions:{}\nHRA:{}", deductions, hra);
        response.setDeductions(Double.toString(deductions + pf));
        response.setHra(Double.toString(hra));
        response.setGrossSalary(Double.toString(grossSalary));
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

    private double newRegimeTax(@NotNull Employee employee, Response response) {
        double taxableSalary = employee.getIncome().tctc();
        double iTax = 0;
        response.setNetSalary(Double.toString(taxableSalary));
        response.setGrossSalary(Double.toString(taxableSalary));
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