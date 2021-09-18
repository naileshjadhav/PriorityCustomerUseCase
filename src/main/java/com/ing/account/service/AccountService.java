/**
 * 
 */
package com.ing.account.service;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ing.account.exception.ResourceNotFoundException;
import com.ing.account.model.AccountDto;
import com.ing.account.model.CustomerDto;
import com.ing.account.repository.Account;
import com.ing.account.repository.AccountRepository;
import com.ing.account.repository.Customer;

/**
 * @author Nailesh
 *
 */
@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	private ModelMapper mapper = new ModelMapper();
	private static final String CREDIT = "credit";
	private static final String DEBIT = "debit";

	/**
	 * @param accountDto
	 * @param amount
	 * @return
	 */
	public AccountDto depositpositiveAmount(AccountDto accountDto, double amount) {
		double total = accountDto.getBalance() + amount;
		accountDto.setBalance(total);
		Account entity = new Account(accountDto.getAccountNumber(), accountDto.getAccountType(),
				accountDto.getBalance(),
				new Customer(accountDto.getCustomerDto().getCustomerId(), accountDto.getCustomerDto().getFirstName(),
						accountDto.getCustomerDto().getLastName(), null, accountDto.getCustomerDto().isPrioritize()),
				LocalDate.now(), CREDIT);
		Account entity1 = accountRepository.save(entity);
		return convertAccountEntityToDto(entity1);
	}

	/**
	 * @param entity
	 * @return
	 */
	private AccountDto convertAccountEntityToDto(Account entity) {
		return mapper.map(entity, AccountDto.class);
	}

	/**
	 * @param accountNumber
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public AccountDto getAccountDetailsByAccountNumber(String accountNumber) throws ResourceNotFoundException {
		Account account = accountRepository.findById(accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found! " + accountNumber));
		CustomerDto customerDto = mapper.map(account.getCustomer(), CustomerDto.class);
		return new AccountDto(account.getAccountNumber(), account.getAccountType(), account.getBalance(), customerDto,
				account.getTransactionDate(), account.getTransactionType());
	}

	/**
	 * @param accountDto
	 * @param amount
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public AccountDto withdrawAmount(AccountDto accountDto, double amount) throws ResourceNotFoundException {
		AccountDto dto = getAccountDetailsByAccountNumber(accountDto.getAccountNumber());
		dto.setBalance(dto.getBalance() - amount);
		dto.setTransactionDate(LocalDate.now());
		dto.setTransactionType(DEBIT);
		Account account = mapper.map(dto, Account.class);
		account = accountRepository.save(account);
		return mapper.map(account, AccountDto.class);
	}

	/**
	 * @param startDate
	 * @param toDate
	 * @param accountNumber
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public AccountDto getAccountTransactionsByDate(LocalDate startDate, LocalDate toDate, String accountNumber)
			throws ResourceNotFoundException {
		Account account = accountRepository.findByTransactionDateAndAccountNumber(startDate, toDate, accountNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found!" + accountNumber));
		return mapper.map(account, AccountDto.class);
	}

}
