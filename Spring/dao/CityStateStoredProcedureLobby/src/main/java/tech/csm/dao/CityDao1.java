package tech.csm.dao;

import java.util.List;

import tech.csm.entity.City1;

public interface CityDao1 {

	String createCity(City1 city);

	List<City1> getAllCities();

}
