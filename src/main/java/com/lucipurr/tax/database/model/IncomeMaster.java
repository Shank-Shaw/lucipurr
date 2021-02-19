package com.lucipurr.tax.database.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "INCOME_MASTER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeMaster implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "incomeId")
    private long incomeId;

    @ManyToOne
    @JoinColumn(name = "empId")
    private EmployeeInfoMaster employeeInfoMaster;

    @Column(name = "basicSalary")
    private double basicSalary;
    @Column(name = "pf")
    private double pf;
    @Column(name = "gratuity")
    private double gratuity;
    @Column(name = "medical")
    private double medical;
    @Column(name = "hra")
    private double hra;
    @Column(name = "lta")
    private double lta;
    @Column(name = "specialAllowances")
    private double specialAllowances;
    @Column(name = "incomeOtherSources")
    private double incomeOtherSources;

}
