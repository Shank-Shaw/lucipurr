package com.lucipurr.tax;


import com.lucipurr.tax.kafka.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class LucipurrApplication {
	public static void main(String[] args) throws InterruptedException {

		ConfigurableApplicationContext context = SpringApplication.run(LucipurrApplication.class, args);

		MessageProducer producer = context.getBean(MessageProducer.class);
		MessageListener listener = context.getBean(MessageListener.class);

		listener.latch.await(10, TimeUnit.SECONDS);
		listener.partitionLatch.await(10, TimeUnit.SECONDS);
		listener.filterLatch.await(10, TimeUnit.SECONDS);
		listener.greetingLatch.await(10, TimeUnit.SECONDS);

		context.close();
	}

	@Bean
	public MessageProducer messageProducer() {
		return new MessageProducer();
	}

	@Bean
	public MessageListener messageListener() {
		return new MessageListener();
	}

	public static class MessageProducer {

		@Autowired
		private KafkaTemplate<String, String> kafkaTemplate;

		@Autowired
		private KafkaTemplate<String, Greeting> greetingKafkaTemplate;

		@Value(value = "${kafka.topic}")
		private String topicName;

		@Value(value = "${partitioned.topic.name}")
		private String partitionedTopicName;

		@Value(value = "${filtered.topic.name}")
		private String filteredTopicName;

		@Value(value = "${greeting.topic.name}")
		private String greetingTopicName;

		public void sendMessage(String message) {

			ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);

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

		public void sendMessageToPartition(String message, int partition) {
			kafkaTemplate.send(partitionedTopicName, partition, null, message);
		}

		public void sendMessageToFiltered(String message) {
			kafkaTemplate.send(filteredTopicName, message);
		}

		public void sendGreetingMessage(Greeting greeting) {
			greetingKafkaTemplate.send(greetingTopicName, greeting);
		}
	}

	public static class MessageListener {

		private CountDownLatch latch = new CountDownLatch(3);

		private CountDownLatch partitionLatch = new CountDownLatch(2);

		private CountDownLatch filterLatch = new CountDownLatch(2);

		private CountDownLatch greetingLatch = new CountDownLatch(1);

		//		@KafkaListener(topics = "${kafka.topic}", groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
//		public void listenGroupFoo(String message) {
//			System.out.println("Received Message in group 'foo': " + message);
//			latch.countDown();
//		}
		@KafkaListener(topics = "${kafka.topic}", groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
		public void listenGroupFooPartition(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
			System.out.println("Received Message in group 'foo': " + message + "from partition: " + partition);
			latch.countDown();
		}

		@KafkaListener(topicPartitions = @TopicPartition(topic = "${partitioned.topic.name}", partitions = {"0", "3"}), containerFactory = "partitionsKafkaListenerContainerFactory")
		public void listenToPartition(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
			System.out.println("Received Message: " + message + " from partition: " + partition);
			this.partitionLatch.countDown();
		}

		@KafkaListener(topics = "${filtered.topic.name}", containerFactory = "filterKafkaListenerContainerFactory")
		public void listenWithFilter(String message) {
			System.out.println("Received Message in filtered listener: " + message);
			this.filterLatch.countDown();
		}

		@KafkaListener(topics = "${greeting.topic.name}", containerFactory = "greetingKafkaListenerContainerFactory")
		public void greetingListener(Greeting greeting) {
			System.out.println("Received greeting message: " + greeting);
			this.greetingLatch.countDown();
		}

	}
}