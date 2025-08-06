package com.eazybytes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eazybytes.model.AccountTransactions;

@Repository
public interface AccountTransactionsRepository extends CrudRepository<AccountTransactions, Long> {
	/* This method is used to find the account transactions based on the customer id
	*  Whenever the user logs in, the account transactions are fetched based on the customer id
	*  AccountTransactions table and Accounts table have a Foreign Key relationship/link based on the customer id
	*  Along with that, the records are sorted based on the transaction date in descending order
	*  */
	List<AccountTransactions> findByCustomerIdOrderByTransactionDtDesc(int customerId);

}