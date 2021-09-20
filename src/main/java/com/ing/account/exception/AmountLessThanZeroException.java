/**
 * This is custom exception for amount less than zero
 */
package com.ing.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Nailesh
 *
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class AmountLessThanZeroException extends RuntimeException {

	/**
	 * @param message
	 */
	public AmountLessThanZeroException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
