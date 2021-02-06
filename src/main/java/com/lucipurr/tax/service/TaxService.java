package com.lucipurr.tax.service;

import com.lucipurr.tax.abstractions.ITaxService;
import com.lucipurr.tax.database.model.EmployeeInfoMaster;
import com.lucipurr.tax.database.repository.EmployeeInfoMasterRepository;
import com.lucipurr.tax.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaxService implements ITaxService {

    @Autowired
    private EmployeeInfoMasterRepository employeeInfoMasterRepository;


    @Override
    public String netTax(Employee employee) {
        double netTax = 0;
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

    @Override
    public String saveData(EmployeeInfoMaster employee) {
        employeeInfoMasterRepository.save(employee);
        return null;

    }

    private double oldRegimeTax(Employee employee) {
        return 0;
    }

    private double newRegimeTax(Employee employee) {
        double taxableSalary = employee.getIncome().tctc();
        double iTax = 0;
        if (taxableSalary < 250000)
            return iTax;
        else {
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
        }
        return iTax;
    }

    private double calculateNewRegime(double salary, double slab, double tax) {
        double iTax = 0;
        while (true) {
            if (slab == 0)
                break;
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

    /*public double calculateHRADeduction(){
        double x = income.getBasicSalary()*(10);
        x /=100;
        double netRentPaid = rentPaid - x;
        return income.getHra() < netRentPaid ? income.getHra() : netRentPaid;
    }*/
}
