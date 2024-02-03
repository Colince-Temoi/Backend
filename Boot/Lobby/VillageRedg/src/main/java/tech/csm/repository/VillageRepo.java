package tech.csm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.entity.Village;

@Repository
public interface VillageRepo extends JpaRepository<Village, Integer> {

	

}
