/**
 * 
 */
package com.ing.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.ing.account.model.AccountDto;
import com.ing.account.model.CustomerDto;
import com.ing.account.repository.Account;
import com.ing.account.repository.Customer;
import com.ing.account.repository.CustomerRepository;
import com.ing.account.service.CustomerService;

/**
 * @author Nailesh
 *
 */
@SpringBootTest
//@DataJpaTest
@ExtendWith(MockitoExtension.class)
class CustomerAccountServiceTest {

	@InjectMocks
	CustomerService service;
	@Mock
	CustomerRepository repo;
	private static List<AccountDto> accountsDto = new ArrayList<AccountDto>();
	private static CustomerDto customerDto = new CustomerDto(1L, "Nil", "Jadhav", addAccountDto(), false);
	private static List<Account> accounts = new ArrayList<Account>();
	private static Customer customer = new Customer(1L, "Nil", "Jadhav", addAccount(), false);

	private static List<Account> addAccount() {
		accounts.add(new Account("SA1001", "Saving", 2000.00, null,LocalDate.now(),null));
		return accounts;
	}

	private static List<AccountDto> addAccountDto() {
		accountsDto.add(new AccountDto("SA1001", "Saving", 2000.00, null,LocalDate.now(),null));
		return accountsDto;
	}

	@Test
	void createCustomerAndAccount() {
		when(repo.save(any(Customer.class))).thenReturn(customer);
		CustomerDto savedCustomer = service.createCustomer(customerDto);
		assertThat(savedCustomer.getFirstName()).isEqualTo("Nil");
		assertThat(savedCustomer.getAccount().get(0).getAccountNumber()).isEqualTo("SA1001");
	}

}
