package tech.csm.service;

import java.util.List;

import tech.csm.dao.PanchayatDao;
import tech.csm.dao.PanchayatDaoImpl;
import tech.csm.entity.Panchayat;

public class PanchayatServiceImpl implements PanchayatService {

	private PanchayatDao panchayatDao;
	
	public PanchayatServiceImpl() {
		panchayatDao=new PanchayatDaoImpl();
	}
	
	
	@Override
	public List<Panchayat> getPanchayatByBlockId(Integer blockId) {
		
		return panchayatDao.getPanchayatByBlockId(blockId);
	}

}
