package com.lucipurr.tax.abstractions;

import com.lucipurr.tax.model.Employee;

public interface ITaxService {
    String netTax(Employee employee);
}
