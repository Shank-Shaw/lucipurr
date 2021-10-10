package com.lucipurr.tax.service;

import com.lucipurr.dbrepo.entity.*;
import com.lucipurr.dbrepo.repository.*;
import com.lucipurr.tax.abstractions.IDataBaseService;
import com.lucipurr.tax.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataBaseService implements IDataBaseService {

    @Autowired
    EmployeeInfoMasterRepository employeeInfoMasterRepository;
    @Autowired
    IncomeMasterRepository incomeMasterRepository;
    @Autowired
    DeductionsMasterRepository deductionsMasterRepository;

    TaxUtil taxUtil = new TaxUtil();

    @Override
    public String save(Employee employee) {
        log.info("\nEmployee:{}\n", employee);
        log.info("\nEmployee INFO MASTER:{}\nIncome Master:{}\n Deduction Master:{}\n", taxUtil.convertEmployeeInfo(employee), taxUtil.convertIncome(employee), taxUtil.convertDeductions(employee));
        employeeInfoMasterRepository.save(taxUtil.convertEmployeeInfo(employee));
        incomeMasterRepository.save(taxUtil.convertIncome(employee));
        deductionsMasterRepository.save(taxUtil.convertDeductions(employee));
        return null;
    }

    @Override
    public Employee fetchDetailsEmpId(String empId) {
        EmployeeInfoMaster employeeInfoMaster = employeeInfoMasterRepository.findByEmpId(empId);
        IncomeMaster incomeMaster = incomeMasterRepository.findByEmpId(empId);
        DeductionsMaster deductionsMaster = deductionsMasterRepository.findByEmpId(empId);
        return new Employee(taxUtil.convertEmployeeInfoMaster(employeeInfoMaster),
                taxUtil.convertIncomeMaster(incomeMaster),
                taxUtil.convertDeductionMaster(deductionsMaster));
    }

}