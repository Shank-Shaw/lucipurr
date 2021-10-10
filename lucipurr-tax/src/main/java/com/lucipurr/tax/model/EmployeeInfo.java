package com.lucipurr.tax.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInfo {
    private String empName;
    private String empId;
    private long age;
    private String email;
    private String location;
    private String regime;
}
