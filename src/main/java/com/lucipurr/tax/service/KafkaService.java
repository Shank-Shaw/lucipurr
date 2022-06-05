package com.lucipurr.tax.service;

import com.lucipurr.tax.kafka.Greeting;
import com.lucipurr.tax.kafka.GreetingBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KafkaService {

    @Value(value = "${kafka.topic}")
    private String topicName;

    @Value(value = "${greeting.topic.name}")
    private String greetingTopicName;
    private KafkaTemplate<String, String> kafkaTemplate;
    private KafkaTemplate<String, Greeting> greetingKafkaTemplate;

    private KafkaTemplate<String, GreetingBatch> batchKafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate,
                        KafkaTemplate<String, Greeting> greetingKafkaTemplate,
                        KafkaTemplate<String, GreetingBatch> batchKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.greetingKafkaTemplate = greetingKafkaTemplate;
        this.batchKafkaTemplate = batchKafkaTemplate;
    }

    public void sendMessage(String message, int partition) {
        ListenableFuture<SendResult<String, String>> future;
        if (partition == -1) {
            future = kafkaTemplate.send(topicName, message);
        } else {
            future = kafkaTemplate.send(topicName, partition, null, message);

        }

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }


    public void sendGreetingMessage(Greeting greeting) {
        greetingKafkaTemplate.send(greetingTopicName, greeting);
    }

    public static void main(String[] args) {
        List<Integer> message = new ArrayList<>();
        for (int i = 0; i <= 10; i++) message.add(i);

        for (int i = 0; i <= 10; i++) {
            log.info("{} -> {}", i, message.get(i));
        }
    }

    public void sendBulkData(Integer id, List<Greeting> message) {
        log.info("Bulk Data : {}", message);
        int hits = (int) Math.ceil((message.size() / 10.0));
        for (int i = 0; i < hits; i++) {
            int start = i * 10;
            GreetingBatch batch = GreetingBatch.builder()
                    .batchId(id)
                    .id(i + 1)
                    .greeting(message.subList(start, start + 10))
                    .build();
            log.info("batch no : {} and batch : {}", batch.getBatchId(), batch);
            batchKafkaTemplate.send(greetingTopicName, batch);
        }
    }
}
