package tech.csm.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.csm.domain.Address;

public interface AddressRepo extends JpaRepository<Address, Integer> {

}
