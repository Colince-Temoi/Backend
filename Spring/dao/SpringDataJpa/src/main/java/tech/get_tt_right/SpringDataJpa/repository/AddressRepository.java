package tech.get_tt_right.SpringDataJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.get_tt_right.SpringDataJpa.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByStreet(String street);
}
