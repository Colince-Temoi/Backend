package tech.csm.service;

import java.util.List;

import tech.csm.entity.CityVo1;

public interface CityService1 {

	String createCity(CityVo1 cityVo);

	List<CityVo1> getAllCities();

}
