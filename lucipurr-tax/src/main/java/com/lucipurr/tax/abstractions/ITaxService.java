package com.lucipurr.tax.abstractions;

import com.lucipurr.tax.model.Response;

public interface ITaxService {
    Response calculateTax(String empId);
}
