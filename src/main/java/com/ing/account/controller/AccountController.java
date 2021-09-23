/**
 * This is customer account controller class
 */
package com.ing.account.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ing.account.exception.AccountBalanceLessThan500Exception;
import com.ing.account.exception.AmountLessThanZeroException;
import com.ing.account.exception.ResourceNotFoundException;
import com.ing.account.model.AccountDto;
import com.ing.account.service.AccountService;

import reactor.core.publisher.Mono;

/**
 * @author Nailesh
 *
 */
@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;

	@PostMapping(value = "/deposit/{amount}")
	@ResponseStatus(value = HttpStatus.OK)
	public Mono<AccountDto> depositPositiveAmount(@RequestBody AccountDto accountDto, @PathVariable double amount) {
		if (amount < 0)
			throw new AmountLessThanZeroException("Amount should be greater then zero!");
		return Mono.just(accountService.depositpositiveAmount(accountDto, amount));
	}

	@PostMapping(value = "/withdraw/{amount}")
	@ResponseStatus(value = HttpStatus.OK)
	public Mono<AccountDto> withdrawAmountFromAccount(@RequestBody AccountDto accountDto, @PathVariable double amount)
			throws ResourceNotFoundException {
		AccountDto accountBalanceDto = accountService.getAccountDetailsByAccountNumber(accountDto.getAccountNumber());
		if (accountBalanceDto.getBalance() < 500.0)
			throw new AccountBalanceLessThan500Exception("Account balance is less then Rs.500 !");
		return Mono.just(accountService.withdrawAmount(accountDto, amount));
	}

	@GetMapping("/statement")
	@ResponseStatus(value = HttpStatus.OK)
	public Mono<AccountDto> accountStatementByDateRange(@RequestParam String startDate, @RequestParam String toDate,
			@RequestParam String accountNumber) throws ResourceNotFoundException {
		return Mono.just(accountService.getAccountTransactionsByDate(LocalDate.parse(startDate), LocalDate.parse(toDate),
				accountNumber));
	}
}
