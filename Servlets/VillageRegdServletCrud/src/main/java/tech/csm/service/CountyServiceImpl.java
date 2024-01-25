package tech.csm.service;

import java.util.List;

import tech.csm.dao.CountyDao;
import tech.csm.dao.CountyDaoImpl;
import tech.csm.entity.County;

public class CountyServiceImpl implements CountyService {

	private CountyDao countyDao;
	
	public CountyServiceImpl() {
		countyDao = new CountyDaoImpl();
	}
	
	@Override
	public List<County> getAllCounties() {
		return countyDao.getAllCounties();
	}

}
