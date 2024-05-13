package tech.csm.service;

import java.util.ArrayList;
import java.util.List;

import tech.csm.dao.CityDao;
import tech.csm.dao.CityDaoImpl;
import tech.csm.entity.City;
import tech.csm.entity.CityVo;
import tech.csm.entity.State;
import tech.csm.entity.StateVo;

public class CityServiceImpl implements CityService {
	
	private CityDao cityDao;
	
	public CityServiceImpl() {
		cityDao = new CityDaoImpl();
	}

	@Override
	public String createCity(CityVo cityVo) {
//		System.out.println(cityVo);
		
//		Convert from Vo type to Dto type
		City city = convertFromVoToDto(cityVo);
		
//		Send this city object to repository layer for persistence
		String msg = cityDao.createCity(city);
			
		return msg;
	}

	private City convertFromVoToDto(CityVo cityVo) {
//		Create City Dto object to hold the things after conversions
		City  city = new City ();
		
//		Set the City Dto related details
		city.setCityId(Integer.parseInt(cityVo.getCityId()));
		city.setCityName(cityVo.getCityName());
		
		State state = new State();
		state.setStateId(Integer.parseInt(cityVo.getStateVo().getStateId()));
		state.setStateName(cityVo.getStateVo().getStateName());
		
		city.setState(state);
		
		return city;
	}

	@Override
	public List<CityVo> getAllCities() {
		
		List<City> cityList = cityDao.getAllCities();
		
//		Convert from dto type to vo type
		List<CityVo> cityVoList = convertFromVoToDto(cityList);
		
		return cityVoList;
	}

	private List<CityVo> convertFromVoToDto(List<City> cityList) {
		
		List<CityVo> cityVoList = new ArrayList<>();
		
		if (!cityList.isEmpty()) {
			for (City city : cityList) {
				CityVo cityVo = new CityVo();
				cityVo.setCityId(city.getCityId().toString());
				cityVo.setCityName(city.getCityName());
				
				StateVo stateVo = new StateVo();
				stateVo.setStateId(city.getState().getStateId().toString());
				stateVo.setStateName(city.getState().getStateName());
				
				cityVo.setStateVo(stateVo);
				
				cityVoList.add(cityVo);
			}
		}
		
		
		return cityVoList;
	}

}
