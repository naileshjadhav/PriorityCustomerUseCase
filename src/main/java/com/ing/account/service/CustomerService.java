/**
 * 
 */
package com.ing.account.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ing.account.model.AccountDto;
import com.ing.account.model.CustomerDto;
import com.ing.account.repository.Account;
import com.ing.account.repository.Customer;
import com.ing.account.repository.CustomerRepository;

/**
 * @author Nailesh
 *
 */

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * @param customerDto
	 * @return customerDto
	 */
	public CustomerDto createCustomer(CustomerDto customerDto) {
		Customer customer = customerRepository.save(customerDtoToEntity(customerDto));
		return customerEntityToDto(customer);
	}

	// We can use model mapper here
	private Customer customerDtoToEntity(CustomerDto dto) {
		List<Account> accounts = new ArrayList<>();
		List<Account> accounts2 = dto.getAccount().stream()
				.map(e -> new Account(e.getAccountNumber(), e.getAccountType(), e.getBalance(), null,e.getTransactionDate(),e.getTransactionType()))
				.collect(Collectors.toList());
		accounts.addAll(accounts2);
		return new Customer(dto.getCustomerId(), dto.getFirstName(), dto.getLastName(), accounts, dto.isPrioritize());
	}

	/**
	 * @param mapper
	 * @param e
	 * @return
	 */

	// We can use model mapper here
	private CustomerDto customerEntityToDto(Customer customer) {
		List<AccountDto> accountsDto = customer.getAccount().stream()
				.map(e -> new AccountDto(e.getAccountNumber(), e.getAccountType(), e.getBalance(), null,e.getTransactionDate(),e.getTransactionType()))
				.collect(Collectors.toList());
		return new CustomerDto(customer.getCustomerId(), customer.getFirstName(), customer.getLastName(), accountsDto,
				customer.isPrioritize());
	}

}
