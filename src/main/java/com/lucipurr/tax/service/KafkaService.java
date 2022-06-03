package com.lucipurr.tax.service;

import com.lucipurr.tax.kafka.Greeting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaService {

    @Value(value = "${kafka.topic}")
    private String topicName;

    @Value(value = "${greeting.topic.name}")
    private String greetingTopicName;
    private KafkaTemplate<String, String> kafkaTemplate;
    private KafkaTemplate<String, Greeting> greetingKafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate,
                        KafkaTemplate<String, Greeting> greetingKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.greetingKafkaTemplate = greetingKafkaTemplate;
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
}
