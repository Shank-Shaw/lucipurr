package com.lucipurr.tax.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {

    @Value(value = "${kafka.topic}")
    String kafkaTopic;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message) {

        kafkaTemplate.send(kafkaTopic, message);
    }

}
