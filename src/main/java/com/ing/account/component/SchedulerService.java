/**
 * 
 */
package com.ing.account.component;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.ing.account.exception.ResourceNotFoundException;
import com.ing.account.repository.Account;
import com.ing.account.repository.AccountRepository;
import com.ing.account.repository.Customer;
import com.ing.account.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Nailesh
 *
 */

@Configuration
@EnableScheduling
@ComponentScan("com.ing.account.component")
@Slf4j
public class SchedulerService {

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	CustomerRepository customerRepository;

	/**
	 * @return boolean
	 * @throws ResourceNotFoundException
	 */
	@Scheduled(cron = "0 5 * * * ?")
	public boolean priorityCustomerSchedule() throws ResourceNotFoundException {
		log.info("priorityCustomerSchedule started....");
		List<Account> accounts = accountRepository.getAllUsingAccontBalance()
				.orElseThrow(() -> new ResourceNotFoundException("No account found having balance less than 10k!"));
		accounts.stream().forEach(
				account -> log.info("non priorityCustomerSchedule accountNumber found: " + account.getAccountNumber()));
		List<Customer> customers = accounts.stream().map(e -> new Customer(e.getCustomer().getCustomerId(),
				e.getCustomer().getFirstName(), e.getCustomer().getLastName(), accounts, false))
				.collect(Collectors.toList());
		customers.stream().forEach(customer -> log
				.info("priorityCustomerSchedule customer to non prioritize are: " + customer.getFirstName()));
		customerRepository.saveAll(customers);
		log.info("priorityCustomerSchedule end....");
		return true;
	}

}
