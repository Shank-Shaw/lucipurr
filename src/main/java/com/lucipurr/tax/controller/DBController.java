package com.lucipurr.tax.controller;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    @ApiOperation(value = "SaveEmployeeAPI", notes = "Save new entity in FAQ's.")
    @GetMapping(value = "/demo/slicing")
    public ClientResponseVO<List<Greeting>> makeslicedData(@RequestParam(value = "count") int count) {
        List<Greeting> list = buildDemoData(count);
        List<List<Greeting>> slices = new ArrayList<>();
        slices = Lists.partition(list, 10);
        for (List<Greeting> currentUserBatch : slices)
            log.info("Batch is {}", currentUserBatch);
        return ClientResponseVO.<List<Greeting>>created()
                .desc("Employee Details for Saved.")
                .data(list)
                .build();
    }

    @ApiOperation(value = "SaveEmployeeAPI", notes = "Save new entity in FAQ's.")
    @GetMapping(value = "/demo/map")
    public ClientResponseVO<Map<Integer, String>> makeSlicedMap(@RequestParam(value = "count") int count) {
        Map<Integer, String> map = buildDemoMap(count);
        return ClientResponseVO.<Map<Integer, String>>created()
                .desc("Employee Details for Saved.")
                .data(map)
                .build();
    }

    private Map<Integer, String> buildDemoMap(int count) {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            map.put(i, UUID.randomUUID().toString());
        }
        log.info("Map Data : {}", map);
        int recordCount = 8;

        AtomicInteger ai = new AtomicInteger(1);
        for (int i = 1; i <= count; i++) {
            log.info("Atomic Integer : {}", ai.getAndIncrement());
        }

        List<List<Map.Entry<Integer, String>>> list = Lists.newArrayList(Iterables.partition(map.entrySet(), recordCount));
        log.info("List of map Chunks : {}", list);
        List<Map<Integer, String>> wowList = new ArrayList<>();
        for (List<Map.Entry<Integer, String>> entry : list) {
            Map<Integer, String> justMap = entry.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            log.info("segmented Map : {}", justMap);
            wowList.add(justMap);
        }
        log.info("Wow List : {}", wowList);
        return map;
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