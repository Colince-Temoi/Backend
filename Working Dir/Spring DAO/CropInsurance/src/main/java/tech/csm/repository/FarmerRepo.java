package tech.csm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.csm.domain.Farmer;

public interface FarmerRepo extends JpaRepository<Farmer, Integer> {

}
