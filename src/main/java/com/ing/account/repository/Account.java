/**
 * This is Customer Account entity class
 */
package com.ing.account.repository;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
public class Account {
	@Id
	private String accountNumber;
	private String accountType;
	private double balance;
	@ManyToOne
	private Customer customer;
	private LocalDate transactionDate;
	private String transactionType;

}
