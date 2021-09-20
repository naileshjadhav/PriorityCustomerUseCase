/**
 * 
 */
package com.ing.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Nailesh
 *
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class AccountBalanceLessThan500Exception extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public AccountBalanceLessThan500Exception(String message) {
		super(message);
	}

}
