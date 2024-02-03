package tech.csm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.csm.entity.Constituency;

public interface ConstituencyRepo extends JpaRepository<Constituency, Integer> {
	
	@Query(value = "from Constituency c where c.county.countyId=:cId")
	List<Constituency> findByCounty_countyId(@Param("cId")Integer countyId);
}
