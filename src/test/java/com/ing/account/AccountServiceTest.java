/**
 * This is account test class
 */
package com.ing.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import com.ing.account.exception.ResourceNotFoundException;
import com.ing.account.model.AccountDto;
import com.ing.account.model.CustomerDto;
import com.ing.account.repository.Account;
import com.ing.account.repository.AccountRepository;
import com.ing.account.repository.Customer;
import com.ing.account.service.AccountService;

/**
 * @author Nailesh
 *
 */
@SpringBootTest
//@DataJpaTest
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	private static final String CREDIT = "credit";
	private static final String DEBIT = "debit";
	@InjectMocks
	AccountService accountService;
	@Mock
	AccountRepository accountRepository;
	ModelMapper mapper = new ModelMapper();

	private static List<AccountDto> accountsDto = new ArrayList<AccountDto>();
	private static CustomerDto customerDto = new CustomerDto(1L, "Nil", "Jadhav", addAccountDto(), false);
	private static List<Account> accounts = new ArrayList<Account>();
	private Customer customer = new Customer(1L, "Nil", "Jadhav", addAccount(), false);
	private Account account = new Account("SA1001", "Saving", 1900.0, customer, LocalDate.now(), null);
	private Account depositAccount = new Account("SA1001", "Saving", 2100.0, customer, LocalDate.now(), CREDIT);
	// private static Account withDrawAccount = new Account("SA1001", "Saving",
	// 1900.0, new Customer(1L, "Nil", "Jadhav", null, false));
	private static AccountDto accountDto = new AccountDto("SA1001", "Saving", 2000.0, customerDto, LocalDate.now(),
			null);
	private static double amount = 100.0;

	private List<Account> addAccount() {
		accounts.add(account);
		return accounts;
	}

	private static List<AccountDto> addAccountDto() {
		accountsDto.add(new AccountDto("SA1001", "Saving", 2000.0, null, LocalDate.now(), null));
		return accountsDto;
	}

	@Test
	void depositAmountToAccount() {
		when(accountRepository.save(any(Account.class))).thenReturn(depositAccount);
		AccountDto savedAccountDto = accountService.depositpositiveAmount(accountDto, amount);
		assertEquals(2100.0, savedAccountDto.getBalance());
	}

	@Test
	void withDrawAmountToAccount() throws ResourceNotFoundException {
		Account newAccount = new Account("SA1001", "Saving", 2200.0, new Customer(1L, "Nil", "Jadhav", null, false),
				LocalDate.now(), DEBIT);
		when(accountRepository.findById("SA1001")).thenReturn(Optional.of(newAccount));
		when(accountRepository.save(any(Account.class))).thenReturn(depositAccount);
		AccountDto savedAccountDto = accountService.withdrawAmount(mapper.map(newAccount, AccountDto.class), amount);
		assertEquals(2100.0, savedAccountDto.getBalance());
	}

	@Test
	void getAccountDetails() throws ResourceNotFoundException {
		when(accountRepository.findById("SA1001")).thenReturn(Optional.of(new Account("SA1001", "Saving", 1900.0,
				new Customer(1L, "Nil", "Jadhav", null, false), LocalDate.now(), null)));
		AccountDto acountDto = accountService.getAccountDetailsByAccountNumber("SA1001");
		assertEquals(1900.0, acountDto.getBalance());
	}

	@Test
	void getAccountTransactionByDate() throws ResourceNotFoundException {
		when(accountRepository.findByTransactionDateAndAccountNumber(LocalDate.of(2021, 5, 1), LocalDate.of(2021, 7, 1),
				"SA1001"))
						.thenReturn(Optional.of(new Account("SA1001", "Saving", 1900.0,
								new Customer(1L, "Nil", "Jadhav", null, false), LocalDate.now(), null)));
		AccountDto acountDto = accountService.getAccountTransactionsByDate(LocalDate.of(2021, 5, 1),
				LocalDate.of(2021, 7, 1), "SA1001");
		assertEquals(1900.0, acountDto.getBalance());
	}
}
