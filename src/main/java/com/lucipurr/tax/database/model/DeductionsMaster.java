package com.lucipurr.tax.database.model;

import com.lucipurr.tax.model.Deductions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DEDUCTION_MASTER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductionsMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "deductionId")
    private long deductionId;
    @ManyToOne
    @JoinColumn(name = "empId")
    private EmployeeInfoMaster employeeInfoMaster;
    @Column(name = "investment80C")
    private double investment80C;
    @Column(name = "medical80D")
    private double medical80D;
    @Column(name = "interestHomeLoan")
    private double interestHomeLoan;
    @Column(name = "investment80E")
    private double investment80E;
    @Column(name = "donations80G")
    private double donations80G;
    @Column(name = "savings80TTA")
    private double savings80TTA;
    @Column(name = "interest80EE")
    private double interest80EE;
    @Column(name = "investment80U")
    private double investment80U;
    @Column(name = "ltaPaid")
    private double ltaPaid;
    @Column(name = "rentPaid")
    private double rentPaid;

    public DeductionsMaster(Deductions deductions) {

    }

}
