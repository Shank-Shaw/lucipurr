package com.lucipurr.tax.controller;

import com.lucipurr.tax.abstractions.IDataBaseService;
import com.lucipurr.tax.database.repository.DeductionsMasterRepository;
import com.lucipurr.tax.database.repository.EmployeeInfoMasterRepository;
import com.lucipurr.tax.database.repository.IncomeMasterRepository;
import com.lucipurr.tax.kafka.Greeting;
import com.lucipurr.tax.model.ClientResponseVO;
import com.lucipurr.tax.model.Employee;
import com.lucipurr.tax.service.DataBaseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/database")
@Slf4j
@RestController
@CrossOrigin
public class DBController {

    IDataBaseService IDataBaseservice;

    @Autowired
    public DBController(EmployeeInfoMasterRepository infoMasterRepository, IncomeMasterRepository incomeMasterRepository, DeductionsMasterRepository deductionsMasterRepository) {
        this.IDataBaseservice = new DataBaseService(infoMasterRepository, incomeMasterRepository, deductionsMasterRepository);
    }

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
                .<String>created()
                .desc("Employee Details for Saved.")
                .data(IDataBaseservice.save(employee))
                .build();
    }

    @ApiOperation(value = "SaveEmployeeAPI", notes = "Save new entity in FAQ's.")
    @GetMapping(value = "/demo")
    public ClientResponseVO<List<Greeting>> makeDemoData(@RequestParam(value = "count") int count) {
        return ClientResponseVO
                .<List<Greeting>>ok()
                .desc("Employee Details for Saved.")
                .data(buildDemoData(count))
                .build();
    }

    private List<Greeting> buildDemoData(int count) {
        List<Greeting> greetingList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            greetingList.add(Greeting.builder()
                    .id(i)
                    .name(UUID.randomUUID().toString())
                    .build());
        }
        return greetingList;
    }


}