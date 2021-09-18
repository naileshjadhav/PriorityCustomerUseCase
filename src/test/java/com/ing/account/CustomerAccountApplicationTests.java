package com.ing.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerAccountApplicationTests {

	@Value("${spring.application.name}")
	private String applicationName;
	
	@Test
	void contextLoads() {
		assertEquals("CustomerAccountUseCase", applicationName);
	}
	
}
