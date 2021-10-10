package com.lucipurr.tax.controller;

import com.lucipurr.dbrepo.repository.*;
import com.lucipurr.tax.abstractions.IDataBaseService;
import com.lucipurr.tax.model.ClientResponseVO;
import com.lucipurr.tax.model.Employee;
import com.lucipurr.tax.service.DataBaseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/database")
@Slf4j
@RestController
@CrossOrigin
public class DBController {

    @Autowired
    IDataBaseService IDataBaseservice;

    @ApiOperation(value = "fetchEmployeeAPI", notes = "Fetch ALL FAQ's by types")
    @GetMapping(value = "/fetch/{empId}", produces = APPLICATION_JSON_VALUE)
    public ClientResponseVO<Employee> fetch(@PathVariable(name = "empId") String empId) {
        log.info("Fetching Employee Details for empId:{}", empId);
        return ClientResponseVO
                .<Employee>ok()
                .desc("Employee Details for Fetched:" + empId + ".\n")
                .data(IDataBaseservice.fetchDetailsEmpId(empId))
                .build();
    }

    @ApiOperation(value = "SaveEmployeeAPI", notes = "Save new entity in FAQ's.")
    @PostMapping(value = "/save")
    public ClientResponseVO<String> save(@RequestBody Employee employee) {
        return ClientResponseVO
                .<String>ok()
                .desc("Employee Details for Saved.")
                .data(IDataBaseservice.save(employee))
                .build();
    }

}