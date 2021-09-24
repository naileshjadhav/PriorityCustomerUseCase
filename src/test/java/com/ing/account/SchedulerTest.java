/**
 * 
 */
package com.ing.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ing.account.component.SchedulerService;
import com.ing.account.exception.ResourceNotFoundException;
import com.ing.account.repository.Account;
import com.ing.account.repository.AccountRepository;
import com.ing.account.repository.Customer;
import com.ing.account.repository.CustomerRepository;

/**
 * @author Nailesh
 *
 */

@SpringBootTest
//@SpringJUnitConfig(SchedulerService.class)
class SchedulerTest {

	@InjectMocks
	private SchedulerService schedulerService;
	@Mock
	AccountRepository accountRepository;
	@Mock
	CustomerRepository customerRepository;

	@Test
	void schedularTest() throws ResourceNotFoundException {
		List<Account> accounts = new ArrayList<>();
		Customer customer1 = new Customer(1L, "nailesh", "jadhav", null, true);
		Customer customer2 = new Customer(2L, "ashish", "jadhav", null, false);
		Customer customer3 = new Customer(3L, "deepak", "jadhav", null, true);
		accounts.add(new Account("SA1001", "Saving", 11000.0, customer1, LocalDate.now(), null));
		accounts.add(new Account("SA1002", "Saving", 10000.0, customer2, LocalDate.now(), null));
		accounts.add(new Account("SA1003", "Saving", 12000.0, customer3, LocalDate.now(), null));
		when(accountRepository.getAllUsingAccontBalance()).thenReturn(Optional.of(accounts));
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(customer1);
		customers.add(customer2);
		customers.add(customer3);
		when(customerRepository.saveAll(customers)).thenReturn(customers);
		assertThat(schedulerService.priorityCustomerSchedule()).isTrue();
	}
}
