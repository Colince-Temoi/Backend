package tech.csm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.csm.domain.City;

@Repository
public interface CityRepo extends JpaRepository<City, Integer> {

	@Query("from City c where state.stateId=:stateId")
	List<City> findCitiesBytateId(@Param("stateId")Integer stateId);
}
