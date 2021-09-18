/**
 * This is customer entity class
 */
package com.ing.account.repository;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Customer {
	@Id
	private Long customerId;
	private String firstName;
	private String lastName;
	@OneToMany(mappedBy = "customer")
	private List<Account> account;
	private boolean isPrioritize;

}
