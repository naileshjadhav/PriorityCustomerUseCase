/**
 * 
 */
package com.ing.account;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import com.ing.account.component.KafkaConsumer;
import com.ing.account.component.KafkaProducer;

/**
 * @author Nailesh
 *
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class KafkaControllerTest {

	@Autowired
	private KafkaConsumer consumer;

	@Autowired
	private KafkaProducer producer;

	@Value("${test.topic}")
	private String topic;

	/*
	 * 1. Create Prioritize Customers. 2. Prioritize Customers are those customers
	 * who balance more than 10000 rs. 3. If Balance is less than 10000 rs , remove
	 * entry from Prioritize Customer. 4. Run in every 5 mins. 5. Push the names to
	 * Kafka.
	 * 
	 */

	@Test
	void createPriorityCustomer() throws InterruptedException {
		producer.send(topic, "Sending with own simple KafkaProducer");
		consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

		assertThat(consumer.getLatch().getCount(), equalTo(0L));
		assertThat(consumer.getPayload(), containsString("embedded-test-topic"));
		assertThat(consumer.getPayload(), containsString("Sending with own simple KafkaProducer"));
	}
}
