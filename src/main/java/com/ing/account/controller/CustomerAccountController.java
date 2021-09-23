/**
 * This is controller class
 */
package com.ing.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.ing.account.model.CustomerDto;
import com.ing.account.service.CustomerService;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nailesh
 *
 */
@RestController
public class CustomerAccountController {

	@Autowired
	private CustomerService service;

	@PostMapping("/create")
	@ResponseStatus(value = HttpStatus.CREATED)
	public Mono<CustomerDto> createCustomerAndAccount(@RequestBody CustomerDto customer) {
		return Mono.just(service.createCustomer(customer));

	}
}
