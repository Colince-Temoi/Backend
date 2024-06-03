package tech.csm.dao;

import java.util.List;

import tech.csm.entity.Village;

public interface VillageDao {

	String saveVillage(Village v);

	List<Village> getAllVillages(int pageNo, int pageSize);

	List<Village> getVillageByBlockId(Integer bId);

	Long getTableSize();

	List<Village> getAllVillages();

}
