package com.lucipurr.tax.abstractions;

import com.lucipurr.tax.model.Employee;

public interface IDBservice {

    String save(Employee employee);

    Employee fetchDetailsEmpId(String empId);

}
