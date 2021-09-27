/**
 * 
 */
package com.ing.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.ing.account.component.CustomerAccountJpaConfig;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CustomerAccountJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
@SpringBootTest
class H2DBJpaTest {
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private CustomerRepository customerRepository;
	private List<Account> accounts = new ArrayList<Account>();
	private Customer customer = new Customer(1L, "Nil", "Jadhav", addAccount(), false);
	private Customer customerPrioretize = new Customer(1L, "Ashish", "Jadhav", addAccount(), true);
	private Account account1 = new Account("SA1001", "Saving", 2000.00, customer, LocalDate.now(), null);
	private Account account2 = new Account("SA1002", "Saving", 20000.00, customerPrioretize, LocalDate.now(), null);
	@SpyBean
	private SchedulerService schedulerService;

	private List<Account> addAccount() {
		accounts.add(account1);
		accounts.add(account2);
		return accounts;
	}

	@Test
	void givenCustomer_whenSave_thenGetOk() {
		customerRepository.save(customer);
		accountRepository.save(account1);
		Customer customer2 = customerRepository.getById(1L);
		assertEquals("Nil", customer2.getFirstName());
	}

	@Test
	void givenCustomer_whenSave_thenName() {
		customerRepository.save(customerPrioretize);
		accountRepository.save(account2);
		Customer customer2 = customerRepository.getById(1L);
		assertEquals("Ashish", customer2.getFirstName());
	}

	@Test
	void testSchedule() throws ResourceNotFoundException {
		customerRepository.save(customer);
		accountRepository.save(account1);
		customerRepository.save(customerPrioretize);
		accountRepository.save(account2);
		List<Account> accounts =  accountRepository.findAllByBalance(10000).get();
		System.out.println("accounts.size(): "+accounts.size());
		assertThat(schedulerService.priorityCustomerSchedule()).isTrue();
	}
}
