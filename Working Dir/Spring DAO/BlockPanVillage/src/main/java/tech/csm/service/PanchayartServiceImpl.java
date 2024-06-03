package tech.csm.service;

import java.util.List;

import tech.csm.dao.PanchayartDao;
import tech.csm.dao.PanchayartDaoImpl;
import tech.csm.entity.Panchayart;

public class PanchayartServiceImpl implements PanchayartService {

	private PanchayartDao productDao=new PanchayartDaoImpl();
	
	@Override
	public List<Panchayart> getPanchayartByBlockId(Integer blockId) {
		return productDao.getPanchayartByBlockId(blockId);
	}

}
