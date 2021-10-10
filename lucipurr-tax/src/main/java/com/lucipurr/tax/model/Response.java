package com.lucipurr.tax.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private String regime;
    private String netSalary;
    private String deductions;
    private String hra;
    private String totalTax;
    private String grossSalary;
    private String savings;
}
