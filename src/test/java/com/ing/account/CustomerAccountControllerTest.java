/**
 * This is CustomerAccountControllertest class
 */
package com.ing.account;

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
import com.ing.account.controller.CustomerAccountController;
import com.ing.account.model.AccountDto;
import com.ing.account.model.CustomerDto;
import com.ing.account.service.CustomerService;

/**
 * @author Nailesh
 *
 */

@WebMvcTest(CustomerAccountController.class)
class CustomerAccountControllerTest {

	@Autowired
	MockMvc mockMvc;
	@MockBean
	private CustomerService customerService;
	private CustomerDto customerDto = new CustomerDto(1L, "Nailesh", "Jadhav", addAccountDto(), false);
	private static List<AccountDto> accountsDto = new ArrayList<AccountDto>();
	AccountDto accountDto = new AccountDto("SA1001", "Saving", 2000.00, customerDto, LocalDate.now(), null);

	private static List<AccountDto> addAccountDto() {
		accountsDto.add(new AccountDto("SA1001", "Saving", 2000.00, null, LocalDate.now(), null));
		return accountsDto;
	}

	@Test
	void createCustomer() throws Exception {
		when(customerService.createCustomer(customerDto)).thenReturn(customerDto);
		mockMvc.perform(MockMvcRequestBuilders.post("/create").content(asJsonString(customerDto))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Nailesh"));
	}
	
	private static String asJsonString(final Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return objectMapper.writeValueAsString(obj);
	}

}
