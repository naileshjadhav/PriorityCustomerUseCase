/**
 * 
 */
package com.ing.account.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Nailesh
 *
 */
public interface AccountRepository extends CrudRepository<Account, String> {

	/**
	 * @param startDate
	 * @param toDate
	 * @param accountNumber
	 * @return
	 */
	@Query(nativeQuery = true, value = "select a.accountNumber,a.transactionDate,a.transactioType,a.balance from Account a where a.transactionDate=:startDate and transactionDate=:toDate and accountNumber=:accountNumber")
	Optional<Account> findByTransactionDateAndAccountNumber(@Param("startDate") LocalDate startDate,
			@Param("toDate") LocalDate toDate, @Param("accountNumber") String accountNumber);

	/**
	 * @return List<Account>
	 */
	@Query(nativeQuery = true, value = "select a.* from Account a where a.balance<=:balance")
	Optional<List<Account>> findAllByBalance(@Param("balance") double balance);

}
