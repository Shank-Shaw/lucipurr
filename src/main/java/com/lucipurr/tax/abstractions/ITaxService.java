package com.lucipurr.tax.abstractions;

import com.lucipurr.tax.model.Employee;
import com.lucipurr.tax.model.Response;

import javax.validation.constraints.NotNull;

public interface ITaxService {
    Response netTax(@NotNull Employee employee);
}
