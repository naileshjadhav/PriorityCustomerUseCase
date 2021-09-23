/**
 * This is CustomerAccountControllertest class
 */
package com.ing.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ing.account.model.AccountDto;
import com.ing.account.model.CustomerDto;
import com.ing.account.service.CustomerService;

import reactor.core.publisher.Mono;

/**
 * @author Nailesh
 *
 */
//@WebMvcTest(CustomerAccountController.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerAccountControllerTest {

	//@Autowired
	//MockMvc mockMvc;
	@Autowired
	private WebTestClient testClient;
	@MockBean
	private CustomerService customerService;
	private CustomerDto customerDto = new CustomerDto(1L, "Nailesh", "Jadhav", addAccountDto(), false);
	private static List<AccountDto> accountsDto = new ArrayList<AccountDto>();
	AccountDto accountDto = new AccountDto("SA1001", "Saving", 2000.00, customerDto, LocalDate.now(), null);

	private static List<AccountDto> addAccountDto() {
		accountsDto.add(new AccountDto("SA1001", "Saving", 2000.00, null, LocalDate.now(), null));
		return accountsDto;
	}

//	@Test
//	void createCustomer() throws Exception {
//		when(customerService.createCustomer(customerDto)).thenReturn(customerDto);
//		mockMvc.perform(MockMvcRequestBuilders.post("/create").content(asJsonString(customerDto))
//				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Nailesh"));
//	}

	@Test
	void createCustomerMonoNew() throws JsonProcessingException {
		when(customerService.createCustomer(customerDto)).thenReturn(customerDto);
	        testClient.post()
	            .uri("/create")
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(Mono.just(customerDto), CustomerDto.class)
	            .exchange()
	            .expectStatus()
	            .isCreated()
	            .expectBody(CustomerDto.class)
	            .value(dto->assertThat(dto.getFirstName().equals("Nailesh")));
	}
	
	
//	private static String asJsonString(final Object obj) throws JsonProcessingException {
//		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
//				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//		return objectMapper.writeValueAsString(obj);
//	}

}
