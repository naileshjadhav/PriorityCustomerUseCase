/**
 * 
 */
package com.ing.account.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nailesh
 *
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountDto {

	private String accountNumber;
	private String accountType;
	private double balance;
	private CustomerDto customerDto;
	private LocalDate transactionDate;
	private String transactionType;

}
