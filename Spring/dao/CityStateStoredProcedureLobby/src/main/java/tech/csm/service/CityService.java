package tech.csm.service;

import java.util.List;

import tech.csm.entity.CityVo;

public interface CityService {

	String createCity(CityVo cityVo);

	List<CityVo> getAllCities();

}
