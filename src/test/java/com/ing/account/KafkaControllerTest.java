/**
 * 
 */
package com.ing.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.concurrent.ListenableFuture;

import com.ing.account.component.KafkaConsumer;
import com.ing.account.model.AccountDto;
import com.ing.account.model.CustomerDto;

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
	KafkaTemplate<String, CustomerDto> kafkaTemplate;
	@Value("${test.topic}")
	private String topic;
	private CustomerDto customerDto = new CustomerDto(3l, "Nailesh", "Jadhav", addAccountDto(), true);
	private AccountDto accountDto = new AccountDto("SA1001", "Saving", 2000.0,
			new CustomerDto(2l, "Nailesh", "Jadhav", null, true), LocalDate.now(), null);

	private List<AccountDto> addAccountDto() {
		List<AccountDto> accounts = new ArrayList<AccountDto>();
		accounts.add(accountDto);
		return accounts;
	}

	/*
	 * 1. Create Prioritize Customers. 2. Prioritize Customers are those customers
	 * who balance more than 10000 rs. 3. If Balance is less than 10000 rs , remove
	 * entry from Prioritize Customer. 4. Run in every 5 mins. 5. Push the names to
	 * Kafka.
	 * 
	 */

//	@Test
//	void kafkaTest() throws InterruptedException {
//		KafkaProducerLocal kafkaTemplateString = new KafkaProducerLocal();
//		kafkaTemplateString.send("embedded-test-topic", "Sending with own simple KafkaProducer");
//		consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
//		assertThat(consumer.getLatch().getCount(), equalTo(0L));
//		assertEquals("Nailesh", consumer.getPayload().getFirstName());
//	}

	@Test
	void createPriorityCustomer() throws InterruptedException, ExecutionException {
		// KafkaProducerConfig config = new KafkaProducerConfig();
		ListenableFuture<SendResult<String, CustomerDto>> listenableFuture = kafkaTemplate
				.send(new ProducerRecord<String, CustomerDto>(topic, "dto", customerDto));
		assertEquals("Nailesh", listenableFuture.get().getProducerRecord().value().getFirstName());
		consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
		assertEquals(customerDto, consumer.getPayload());
		assertEquals("Nailesh", ((CustomerDto) consumer.getPayload()).getFirstName());
	}
}
