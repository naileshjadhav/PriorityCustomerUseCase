/**
 * 
 */
package com.ing.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ing.account.model.AccountDto;
import com.ing.account.model.CustomerDto;
import com.ing.account.service.AccountService;

import reactor.core.publisher.Mono;

/**
 * @author Nailesh
 *
 */
//@WebMvcTest(AccountController.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

	//@Autowired
	//MockMvc mockMvc;
	@Autowired
	WebTestClient webClient;
	@MockBean
	private AccountService accountService;
	private CustomerDto customerDto = new CustomerDto(1L, "Nailesh", "Jadhav", addAccountDto(), false);
	private static final String CREDIT = "credit";
	private static final String DEBIT = "debit";
	private static List<AccountDto> accountsDto = new ArrayList<AccountDto>();
	private AccountDto accountDto = new AccountDto("SA1001", "Saving", 2000.0, customerDto, LocalDate.now(), null);
	private AccountDto depositDto = new AccountDto("SA1001", "Saving", 2100.0, customerDto, LocalDate.now(), CREDIT);
	private AccountDto withDrawDto = new AccountDto("SA1001", "Saving", 1800.0, customerDto, LocalDate.now(), DEBIT);
	private AccountDto lessBalanceDto = new AccountDto("SA1001", "Saving", 400.0d, customerDto, LocalDate.now(), null);
	private double amount = 100.0;

	private List<AccountDto> addAccountDto() {
		accountsDto.add(accountDto);
		return accountsDto;
	}

	@Test
	void depositPositiveAmount() throws JsonProcessingException, Exception {
		when(accountService.depositpositiveAmount(accountDto, amount)).thenReturn(depositDto);
		webClient.post().uri("/deposit/{amount}", 100.0d).contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(accountDto), AccountDto.class).exchange().expectStatus().isOk()
				.expectBody(AccountDto.class).value(dto -> assertThat(dto.getBalance() == 2100.0));
	}

	@Test
	void depositNegativeAmount() throws JsonProcessingException, Exception {
		webClient.post()
		.uri("/deposit/{amount}", -100.0d)
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(accountDto), AccountDto.class)
		.exchange()
		.expectStatus()
		.isEqualTo(HttpStatus.NOT_ACCEPTABLE)
		.expectBody(String.class).value(string -> containsString("Amount should be greater then zero!"));
	}

	@Test
	void withdrawAmountFromAccount() throws JsonProcessingException, Exception {
		when(accountService.getAccountDetailsByAccountNumber("SA1001")).thenReturn(withDrawDto);
		when(accountService.withdrawAmount(accountDto, amount)).thenReturn(withDrawDto);
		webClient.post().uri("/withdraw/{amount}", amount).contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(accountDto), AccountDto.class)
		.exchange()
		.expectStatus().isOk()
		.expectBody(AccountDto.class).value(dto -> assertThat(dto.getBalance() == 1900.0));
	}

	@Test
	void accountBalanceLessThan500() throws JsonProcessingException, Exception {
		when(accountService.getAccountDetailsByAccountNumber("SA1001")).thenReturn(lessBalanceDto);
		webClient.post().uri("/withdraw/{amount}", amount).contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(accountDto), AccountDto.class)
		.exchange()
		.expectStatus()
		.isEqualTo(HttpStatus.NOT_ACCEPTABLE)
		.expectBody(String.class).value(string -> containsString("Account balance is less then Rs.500 !"));
	}

	@Test
	void showAllTransactionFromAccountByDate() throws JsonProcessingException, Exception {
		when(accountService.getAccountTransactionsByDate(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 9, 1), "SA1001"))
				.thenReturn(accountDto);
		webClient.get()
				.uri(uriBuilder -> uriBuilder
			    .path("/statement")
			    .queryParam("startDate", LocalDate.of(2021, 8, 1).toString())
			    .queryParam("toDate", LocalDate.of(2021, 9, 1).toString())
			    .queryParam("accountNumber", "SA1001")
			    .build())
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(AccountDto.class)
				.value(dto -> assertThat(dto.getBalance() == 2000.0));
	}
	/*
	 * 	@Test
	void depositPositiveAmount() throws JsonProcessingException, Exception {
		when(accountService.depositpositiveAmount(accountDto, amount)).thenReturn(depositDto);
		mockMvc.perform(MockMvcRequestBuilders.post("/deposit/{amount}", 100.0d).content(asJsonString(accountDto))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(2100.0));
	}

	@Test
	void depositNegativeAmount() throws JsonProcessingException, Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/deposit/{amount}", -100.0d).content(asJsonString(accountDto))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable())
				.andExpect(result -> assertEquals("Amount should be greater then zero!",
						result.getResolvedException().getMessage()));
	}

	@Test
	void withdrawAmountFromAccount() throws JsonProcessingException, Exception {
		when(accountService.getAccountDetailsByAccountNumber("SA1001")).thenReturn(withDrawDto);
		when(accountService.withdrawAmount(accountDto, 200.0)).thenReturn(withDrawDto);
		mockMvc.perform(MockMvcRequestBuilders.post("/withdraw/{amount}", 200.0).content(asJsonString(accountDto))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(1800.0));
	}

	@Test
	void accountBalanceLessThan500() throws JsonProcessingException, Exception {
		when(accountService.getAccountDetailsByAccountNumber("SA1001")).thenReturn(lessBalanceDto);
		mockMvc.perform(MockMvcRequestBuilders.post("/withdraw/{amount}", 200.0).content(asJsonString(lessBalanceDto))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable())
				.andExpect(result -> assertEquals("Account balance is less then Rs.500 !",
						result.getResolvedException().getMessage()));
	}

	@Test
	void showAllTransactionFromAccountByDate() throws JsonProcessingException, Exception {
		when(accountService.getAccountTransactionsByDate(LocalDate.of(2021, 5, 1), LocalDate.of(2021, 7, 1), "SA1001"))
				.thenReturn(accountDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/statement").param("startDate", LocalDate.of(2021, 5, 1).toString())
				.param("toDate", LocalDate.of(2021, 7, 1).toString()).param("accountNumber", "SA1001")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(2000.0));
	}

	private static String asJsonString(final Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return objectMapper.writeValueAsString(obj);
	}
	 * 
	 */

}
