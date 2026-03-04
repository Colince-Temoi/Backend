package com.get_tt_right.accounts.repository;

import com.get_tt_right.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Retrieves a customer by their mobile number.
     * The MobileNumber you have mentioned in the finder method string should match the field name in the entity class. Don't worry about the case sensitivity.
     * @param mobileNumber The phone number of the customer to retrieve
     * @return An Optional containing the customer with the specified mobile number, or an empty Optional if no such customer exists
     */
    Optional<Customer> findByMobileNumber(String mobileNumber);


}