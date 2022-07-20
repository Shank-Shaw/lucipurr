package com.lucipurr.tax.controller;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.lucipurr.tax.kafka.Greeting;
import com.lucipurr.tax.kafka.HugeMap;
import com.lucipurr.tax.model.ClientResponseVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequestMapping("/test")
@Slf4j
@RestController
@CrossOrigin
public class TestController {

    String s = "USER_LIST";
    private Map<Integer, HugeMap> justMap = new HashMap<>();

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
    public ClientResponseVO<List<Greeting>> makeSlicedData(@RequestParam(value = "count") int count) {
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

    @ApiOperation(value = "SaveEmployeeAPI", notes = "Save new entity in FAQ's.")
    @GetMapping(value = "/demo/json")
    public ClientResponseVO<String> demoJson(@RequestParam(value = "count") int count) throws JSONException, InterruptedException {
        Map<Integer, String> demoMap = this.buildDemoMap(count);
        JSONObject json = getJsonDemo("demo", demoMap, count, "testTrigger", "007");
        TimeUnit.SECONDS.sleep(15);
        log.info("Json Object : {}", json);
        String jsonString = json.toString();
        log.info("Json String : {}", jsonString);
        JSONObject newJson = new JSONObject(jsonString);
        newJson.remove(s);
        newJson.put(s, demoMap);
        return ClientResponseVO.<String>created()
                .desc("Employee Details for Saved.")
                .data(newJson.toString())
                .build();
    }

    public JSONObject getJsonDemo(String campaignId,
                                  Map<Integer, String> users,
                                  int path,
                                  String triggerName,
                                  String correlationId) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("CAMPAIGN_ID", campaignId);
        object.put("USER_LIST", users);
        object.put("PATH", path);
        object.put("TRIGGER_NAME", triggerName);
        object.put("CORRELATION_ID", correlationId);
        return object;
    }

    @ApiOperation(value = "SaveEmployeeAPI", notes = "Save new entity in FAQ's.")
    @GetMapping(value = "/demo/data")
    public ClientResponseVO<Map<Integer, String>> demoAggregateData(@RequestBody HugeMap data) throws InterruptedException {
        ClientResponseVO<Map<Integer, String>> response = null;
        int id = data.getId();
        if (justMap.containsKey(id)) {
            HugeMap hugeMap = justMap.get(id);
            Map<Integer, String> userMap = hugeMap.getMap();
            Set<Integer> nextSliceOfUsers = data.getMap().keySet();
            nextSliceOfUsers.forEach(item -> userMap.put(item, data.getMap().get(item)));
            if (this.isLastBatch(data)) {
                response = ClientResponseVO.<Map<Integer, String>>ok().desc("Employee Details for Saved.")
                        .data(hugeMap.getMap())
                        .build();
                justMap.remove(id);
            } else {
                response = ClientResponseVO.<Map<Integer, String>>created()
                        .desc("Inserted new Data")
                        .data(data.getMap())
                        .build();
            }
        } else {
            justMap.put(id, data);
            response = ClientResponseVO.<Map<Integer, String>>created()
                    .desc("Inserted")
                    .data(data.getMap())
                    .build();
        }
        return response;

    }

    private Map<Integer, String> buildDemoMap(int count) {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 1; i <= count; i++) {
            map.put(i, UUID.randomUUID().toString());
        }
        log.info("Map Data : {}", map);
        int recordCount = 8;

        List<List<Map.Entry<Integer, String>>> list = Lists.newArrayList(Iterables.partition(map.entrySet(), recordCount));
        log.info("List of map Chunks : {}", list);
        List<Map<Integer, String>> wowList = new ArrayList<>();
        for (List<Map.Entry<Integer, String>> entry : list) {
            Map<Integer, String> mapX = entry.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            log.info("segmented Map : {}", mapX);
            wowList.add(mapX);
        }
        List<Map<Integer, String>> list2 = new ArrayList<>();
        Iterables.partition(map.entrySet(), recordCount).forEach(item -> list2.add(item.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
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

    private boolean isLastBatch(HugeMap message) {
        return message.getBatchSize().equals(message.getSliceId());
    }
}
