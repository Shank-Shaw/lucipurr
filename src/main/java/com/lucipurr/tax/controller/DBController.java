package com.lucipurr.tax.controller;

import com.lucipurr.tax.abstractions.IDBservice;
import com.lucipurr.tax.database.repository.DeductionsMasterRepository;
import com.lucipurr.tax.database.repository.EmployeeInfoMasterRepository;
import com.lucipurr.tax.database.repository.IncomeMasterRepository;
import com.lucipurr.tax.model.Employee;
import com.lucipurr.tax.service.DBService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/database")
@Slf4j
@RestController
@CrossOrigin
public class DBController {

    IDBservice idBservice;

    @Autowired
    public DBController(EmployeeInfoMasterRepository infoMasterRepository, IncomeMasterRepository incomeMasterRepository, DeductionsMasterRepository deductionsMasterRepository) {
        this.idBservice = new DBService(infoMasterRepository, incomeMasterRepository, deductionsMasterRepository);
    }

    @ApiOperation(value = "fetchAllFAQAPI", notes = "Fetch ALL FAQ's by types")
    @GetMapping(value = "/fetch/{empId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> fetch(@PathVariable(name = "empId") String empId) {
        log.info("Fetching Employee Details for empId:{}", empId);
        return ResponseEntity.ok(idBservice.fetchDetailsEmpId(empId));
    }

    @ApiOperation(value = "SaveFAQAPI", notes = "Save new entity in FAQ's.")
    @PostMapping(value = "/save")
    public ResponseEntity<String> save(@RequestBody Employee employee) {

        return ResponseEntity.ok(idBservice.save(employee));
    }

}