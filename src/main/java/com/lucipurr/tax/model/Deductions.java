package com.lucipurr.tax.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Deductions {

    private double standardDeduction = 50000;
    private double rentPaid;
    private double lta; // leave travel Allowances
    private double investment80C;
    private double medical80D;
    private double interestHomeLoan;
    private double investment80E;
    private double donations80G;
    private double savings80TTA;
    private double interest80EE;
    private double investment80U;
    private double ltaDeduction;

    public Deductions(long rentPaid,
                      long lta,
                      long investment80C,
                      long medical80D,
                      long interestHomeLoan,
                      long investment80E,
                      long donations80G,
                      long savings80TTA,
                      long interest80EE,
                      double investment80U,
                      double ltaDeduction) {
        this.rentPaid = rentPaid;
        this.lta = lta;
        this.investment80C = investment80C;
        this.medical80D = medical80D;
        this.interestHomeLoan = interestHomeLoan;
        this.investment80E = investment80E;
        this.donations80G = donations80G;
        this.savings80TTA = savings80TTA;
        this.interest80EE = interest80EE;
        this.investment80U = investment80U;
        this.ltaDeduction = ltaDeduction;
    }

    public double netExcemption() {
        return standardDeduction + rentPaid + lta + investment80C + medical80D +
                interestHomeLoan + investment80E + donations80G + savings80TTA +
                interest80EE + investment80U + ltaDeduction;
    }
}
