package com.lucipurr.tax.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "EMP_MASTER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInfoMaster {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Id
    @Column(name = "empId")
    private String empId;

    @Column(name = "empName")
    private String empName;
    @Column(name = "email")
    private String email;
    @Column(name = "age")
    private long age;
    @Column(name = "location")
    private String location;
    @Column(name = "regime")
    private String regime;

}
