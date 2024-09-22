package com.eazybytes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eazybytes.model.Accounts;

@Repository
public interface AccountsRepository extends CrudRepository<Accounts, Long> {
	/* This method is used to find the account details based on the customer id
	* Whenever the user logs in, the account details are fetched based on the customer id
	* Accounts table and Customer table have a Foreign Key relationship/link based on the customer id
	* How I will know the customerId is, as soon as the login is completed, I can easily understand the customerId of my end user.
	* */
	Accounts findByCustomerId(int customerId);

}