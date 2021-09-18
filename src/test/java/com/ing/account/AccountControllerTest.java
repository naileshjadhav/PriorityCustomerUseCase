/**
 * 
 */
package com.ing.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ing.account.controller.AccountController;
import com.ing.account.model.AccountDto;
import com.ing.account.model.CustomerDto;
import com.ing.account.service.AccountService;

/**
 * @author Nailesh
 *
 */
@WebMvcTest(AccountController.class)
class AccountControllerTest {

	@Autowired
	MockMvc mockMvc;
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
//		mockMvc.perform(MockMvcRequestBuilders.get("/statement").param("startDate", LocalDate.of(2021, 5, 1).toString())
//				.param("toDate", LocalDate.of(2021, 7, 1).toString()).param("accountNumber", "SA1001")
//				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(2000.0));
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
}
