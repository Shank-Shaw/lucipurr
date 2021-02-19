package com.lucipurr.tax.service;

import com.lucipurr.tax.database.model.DeductionsMaster;
import com.lucipurr.tax.database.model.EmployeeInfoMaster;
import com.lucipurr.tax.database.model.IncomeMaster;
import com.lucipurr.tax.model.Deductions;
import com.lucipurr.tax.model.Employee;
import com.lucipurr.tax.model.EmployeeInfo;
import com.lucipurr.tax.model.Income;

public class TaxUtil {

    /**
     * These are conversion functions which have one purpose, to convert Json POJO Object to Repository Object
     */
    public DeductionsMaster convertDeductions(Employee employee) {
        Deductions deductions = employee.getDeductions();
        DeductionsMaster deductionsMaster = new DeductionsMaster();
        deductionsMaster.setDonations80G(deductions.getDonations80G());
        deductionsMaster.setInterest80EE(deductions.getInterest80EE());
        deductionsMaster.setInterestHomeLoan(deductions.getInterestHomeLoan());
        deductionsMaster.setInvestment80C(deductions.getInvestment80C());
        deductionsMaster.setInvestment80E(deductions.getInvestment80E());
        deductionsMaster.setInvestment80U(deductions.getInvestment80U());
        deductionsMaster.setLtaPaid(deductions.getLtaPaid());
        deductionsMaster.setMedical80D(deductions.getMedical80D());
        deductionsMaster.setRentPaid(deductions.getRentPaid());
        deductionsMaster.setSavings80TTA(deductions.getSavings80TTA());
        deductionsMaster.setEmployeeInfoMaster(convertEmployeeInfo(employee));
        return deductionsMaster;
    }

    public EmployeeInfoMaster convertEmployeeInfo(Employee employee) {

        EmployeeInfo employeeInfo = employee.getEmp();
        EmployeeInfoMaster employeeInfoMaster = new EmployeeInfoMaster();

        employeeInfoMaster.setEmpId(employeeInfo.getEmpId());
        employeeInfoMaster.setEmpName(employeeInfo.getEmpName());
        employeeInfoMaster.setEmail(employeeInfo.getEmail());
        employeeInfoMaster.setAge(employeeInfo.getAge());
        employeeInfoMaster.setLocation(employeeInfo.getLocation());
        employeeInfoMaster.setRegime(employeeInfo.getRegime());
        return employeeInfoMaster;
    }

    public IncomeMaster convertIncome(Employee employee) {
        Income income = employee.getIncome();
        IncomeMaster incomeMaster = new IncomeMaster();
        incomeMaster.setBasicSalary(income.getBasicSalary());
        incomeMaster.setGratuity(income.getGratuity());
        incomeMaster.setPf(income.getPf());
        incomeMaster.setIncomeOtherSources(income.getIncomeOtherSources());
        incomeMaster.setHra(income.getHra());
        incomeMaster.setLta(income.getLta());
        incomeMaster.setMedical(income.getMedical());
        incomeMaster.setSpecialAllowances(income.getSpecialAllowances());
        incomeMaster.setEmployeeInfoMaster(convertEmployeeInfo(employee));
        return incomeMaster;
    }

    /**
     * These are conversion functions which have one purpose,
     * to convert Repository Object to POJO object which we use/send for further steps required.
     */
    public EmployeeInfo convertEmployeeInfoMaster(EmployeeInfoMaster employeeInfoMaster) {
        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo.setEmpId(employeeInfoMaster.getEmpId());
        employeeInfo.setEmpName(employeeInfoMaster.getEmpName());
        employeeInfo.setAge(employeeInfoMaster.getAge());
        employeeInfo.setEmail(employeeInfo.getEmail());
        employeeInfo.setLocation(employeeInfoMaster.getLocation());
        employeeInfo.setRegime(employeeInfoMaster.getRegime());
        return employeeInfo;
    }

    public Deductions convertDeductionMaster(DeductionsMaster deductionsMaster) {
        Deductions deductions = new Deductions();
        deductions.setDonations80G(deductionsMaster.getDonations80G());
        deductions.setInterest80EE(deductionsMaster.getInterest80EE());
        deductions.setInterestHomeLoan(deductionsMaster.getInterestHomeLoan());
        deductions.setInvestment80C(deductionsMaster.getInvestment80C());
        deductions.setInvestment80E(deductionsMaster.getInvestment80E());
        deductions.setInvestment80U(deductionsMaster.getInvestment80U());
        deductions.setLtaPaid(deductionsMaster.getLtaPaid());
        deductions.setMedical80D(deductionsMaster.getMedical80D());
        deductions.setRentPaid(deductionsMaster.getRentPaid());
        deductions.setSavings80TTA(deductionsMaster.getSavings80TTA());
        return deductions;
    }

    public Income convertIncomeMaster(IncomeMaster incomeMaster) {
        Income income = new Income();
        income.setBasicSalary(incomeMaster.getBasicSalary());
        income.setGratuity(incomeMaster.getGratuity());
        income.setPf(incomeMaster.getPf());
        income.setIncomeOtherSources(incomeMaster.getIncomeOtherSources());
        income.setHra(incomeMaster.getHra());
        income.setLta(incomeMaster.getLta());
        income.setMedical(incomeMaster.getMedical());
        income.setSpecialAllowances(incomeMaster.getSpecialAllowances());
        return income;
    }

}