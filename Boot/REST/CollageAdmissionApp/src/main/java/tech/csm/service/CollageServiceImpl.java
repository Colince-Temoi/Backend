package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.dao.Collagedao;
import tech.csm.domain.Collage;

@Service
public class CollageServiceImpl implements CollageService{
	
	@Autowired
	private Collagedao collageDao;

	@Override
	public List<Collage> findAllCollages() {
		List<Collage> collageList = collageDao.findAllCollages();
		return collageList;
	}


}
