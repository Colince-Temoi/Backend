package tech.csm.service;

import java.util.List;

import tech.csm.dao.VillageDao;
import tech.csm.dao.VillageDaoImpl;
import tech.csm.entity.Village;

public class VillageServiceImpl implements VillageService {
 
	private VillageDao  villageDao;
	public VillageServiceImpl() {
		villageDao=new VillageDaoImpl();
	}
	@Override
	public String saveVillage(Village v) {
		
		return villageDao.saveVillage(v);
	}
	@Override
	public List<Village> getAllVillages(int pageNo, int pageSize) {
		
		return villageDao.getAllVillages(pageNo, pageSize);
	}
	@Override
	public List<Village> getVillageByBlockId(Integer bId) {
		return villageDao.getVillageByBlockId(bId);
	}
	@Override
	public Long getTableSize() {
		
		return villageDao.getTableSize();
	}
	@Override
	public List<Village> getAllVillages() {
		return villageDao.getAllVillages();
	}
	
}
