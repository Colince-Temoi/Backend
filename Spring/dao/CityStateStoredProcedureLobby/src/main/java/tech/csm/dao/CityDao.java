package tech.csm.dao;

import java.util.List;

import tech.csm.entity.City;

public interface CityDao {

	String createCity(City city);

	List<City> getAllCities();

}
