package com.lucipurr.tax.controller;

import com.lucipurr.tax.abstractions.ITaxService;
import com.lucipurr.tax.model.ClientResponseVO;
import com.lucipurr.tax.model.Response;
import com.lucipurr.tax.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/tax")
@Slf4j
@RestController
@CrossOrigin
public class TaxController {
    @Autowired
    ITaxService iTaxService;
    @Autowired
    KafkaService kafkaService;
    @Autowired
    HttpServletRequest httpServletRequest;

    @GetMapping(value = "/calculate/{empId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientResponseVO<Response> saveData(@PathVariable String empId) {
        return ClientResponseVO
                .<Response>ok()
                .desc("Tax Calculated.")
                .data(iTaxService.calculateTax(empId))
                .build();
    }

    @GetMapping(value = "/producer")
    public String producer(@RequestParam("message") String message,
                           @RequestParam(value = "partition", required = false) String partition) {
        if (httpServletRequest.getParameter("partition") == null) {
            kafkaService.sendMessage(message, -1);
        } else {
            kafkaService.sendMessage(message, Integer.parseInt(partition));
        }
        return "Message sent to the Kafka Topic java_in_use_topic Successfully";
    }
}
