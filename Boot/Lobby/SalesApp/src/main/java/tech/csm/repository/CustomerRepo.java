package tech.csm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

}
