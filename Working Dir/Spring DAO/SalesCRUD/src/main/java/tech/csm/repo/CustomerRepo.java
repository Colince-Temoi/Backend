package tech.csm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

}
