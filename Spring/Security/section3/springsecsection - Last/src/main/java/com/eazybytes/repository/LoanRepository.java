package com.eazybytes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import com.eazybytes.model.Loans;

@Repository
public interface LoanRepository extends CrudRepository<Loans, Long> {
	/* This method is used to find the loan details based on the customer id
	*  Whenever the user logs in, the loan details are fetched based on the customer id
	*  Loans table and Customer table have a Foreign Key relationship/link based on the customer id
	*  Along with that, the records are sorted based on the start date in descending order
	*  */
//	@PreAuthorize("hasRole('USER')")
	List<Loans> findByCustomerIdOrderByStartDtDesc(int customerId);

}