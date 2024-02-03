package tech.csm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.entity.County;

@Repository
public interface CountyRepo extends JpaRepository<County, Integer> {

	
}
