package com.get_tt_right.customer.repository;

import com.get_tt_right.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByMobileNumberAndActiveSw(String mobileNumber,boolean active);


}
