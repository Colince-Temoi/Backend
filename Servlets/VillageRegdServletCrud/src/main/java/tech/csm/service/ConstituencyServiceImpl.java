package tech.csm.service;

import java.util.List;

import tech.csm.dao.ConstituencyDao;
import tech.csm.dao.ConstituencyDaoImpl;
import tech.csm.entity.Constituency;

public class ConstituencyServiceImpl implements ConstituencyService {

	ConstituencyDao constituencyDao;
	
	public ConstituencyServiceImpl() {
		constituencyDao = new ConstituencyDaoImpl();
	}
	
	@Override
	public List<Constituency> getConstituencieByCountyId(Integer countyId) {
		return constituencyDao.getConstituencieByCountyId(countyId);
	}

}
