package com.lucipurr.tax.abstractions;

import com.lucipurr.tax.database.model.EmployeeInfoMaster;
import com.lucipurr.tax.model.Employee;

public interface ITaxService {
    String netTax(Employee employee);

    String saveData(EmployeeInfoMaster employee);
}
