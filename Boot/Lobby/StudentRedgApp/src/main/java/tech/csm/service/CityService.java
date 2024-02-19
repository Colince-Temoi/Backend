package tech.csm.service;

import java.util.List;

import tech.csm.domain.City;

public interface CityService {

	List<City> findCitiesByStateId(Integer stateId);

}
