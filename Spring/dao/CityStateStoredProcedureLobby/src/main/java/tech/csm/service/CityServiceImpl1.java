package tech.csm.service;

import java.util.ArrayList;
import java.util.List;

import tech.csm.dao.CityDao1;
import tech.csm.dao.CityDaoImpl1;
import tech.csm.entity.City1;
import tech.csm.entity.CityVo1;
import tech.csm.entity.State1;
import tech.csm.entity.StateVo1;

public class CityServiceImpl1 implements CityService1 {
	
	private CityDao1 cityDao;
	
	public CityServiceImpl1() {
		cityDao = new CityDaoImpl1();
	}

	@Override
	public String createCity(CityVo1 cityVo) {
//		System.out.println(cityVo);
		
//		Convert from Vo type to Dto type
		City1 city = convertFromVoToDto(cityVo);
		
//		Send this city object to repository layer for persistence
		String msg = cityDao.createCity(city);
			
		return msg;
	}

	private City1 convertFromVoToDto(CityVo1 cityVo) {
//		Create City Dto object to hold the things after conversions
		City1  city = new City1 ();
		
//		Set the City Dto related details
		city.setCityId(Integer.parseInt(cityVo.getCityId()));
		city.setCityName(cityVo.getCityName());
		
		State1 state = new State1();
		state.setStateId(Integer.parseInt(cityVo.getStateVo().getStateId()));
		state.setStateName(cityVo.getStateVo().getStateName());
		
		city.setState(state);
		
		return city;
	}

	@Override
	public List<CityVo1> getAllCities() {
		
		List<City1> cityList = cityDao.getAllCities();
		
//		Convert from dto type to vo type
		List<CityVo1> cityVoList = convertFromVoToDto(cityList);
		
		return cityVoList;
	}

	private List<CityVo1> convertFromVoToDto(List<City1> cityList) {
		
		List<CityVo1> cityVoList = new ArrayList<>();
		
		if (!cityList.isEmpty()) {
			for (City1 city : cityList) {
				CityVo1 cityVo = new CityVo1();
				cityVo.setCityId(city.getCityId().toString());
				cityVo.setCityName(city.getCityName());
				
				StateVo1 stateVo = new StateVo1();
				stateVo.setStateId(city.getState().getStateId().toString());
				stateVo.setStateName(city.getState().getStateName());
				
				cityVo.setStateVo(stateVo);
				
				cityVoList.add(cityVo);
			}
		}
		
		
		return cityVoList;
	}

}
