package com.lucipurr.tax.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Income {
    private double basicSalary;
    private double pf;
    private double gratuity;
    private double medical;
    private double hra;
    private double lta;
    private double specialAllowances;
    private double incomeOtherSources;

    public double tctc() {
        return (basicSalary + pf + gratuity + medical + hra + specialAllowances + incomeOtherSources + lta);
    }
}
