/**
 * 
 */
package com.ing.account.component;

import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Nailesh
 *
 */
@Component
public class KafkaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

	private CountDownLatch latch = new CountDownLatch(1);
	private Object payload;

	@KafkaListener(topics = "${test.topic}", groupId = "${group.id}", containerFactory = "kafkaListenerContainerFactory")
	public void receive(ConsumerRecord<?, ?> consumerRecord) {
		LOGGER.info("received payload='{}'", consumerRecord);
		setPayload(consumerRecord.value());
		latch.countDown();
	}

	/**
	 * @param string
	 */
	private void setPayload(Object object) {
		this.payload = object;
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public Object getPayload() {
		return payload;
	}
}
