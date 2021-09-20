/**
 * 
 */
package com.ing.account.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nailesh
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomerDto {

	private Long customerId;
	private String firstName;
	private String lastName;
	private List<AccountDto> account;
	private boolean isPrioritize;
}
