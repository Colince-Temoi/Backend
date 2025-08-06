package com.eazybytes.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eazybytes.model.Cards;

@Repository
public interface CardsRepository extends CrudRepository<Cards, Long> {
	/* This method is used to find the card details based on the customer id
	*  Whenever the user logs in, the card details are fetched based on the customer id
	* */
	List<Cards> findByCustomerId(int customerId);

}