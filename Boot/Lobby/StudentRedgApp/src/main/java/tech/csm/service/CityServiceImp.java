package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.domain.City;
import tech.csm.repo.CityRepo;

@Service
public class CityServiceImp implements CityService {

	@Autowired
	private CityRepo cityRepo;
	
	@Override
	public List<City> findCitiesByStateId(Integer stateId) {
		List<City> cities = cityRepo.findCitiesBytateId(stateId);
		
		return cities;		
	}	

}
