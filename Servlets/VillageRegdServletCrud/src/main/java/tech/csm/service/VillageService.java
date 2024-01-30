package tech.csm.service;

import java.util.List;

import tech.csm.entity.Village;

public interface VillageService {

	String saveVillage(Village v);

	List<Village> getAllVillages(Integer pageNo, Integer pageSize);

	List<Village> getVillageByBlockId(Integer bId);

	Long getTableSize();

	List<Village> getAllVillages();

}
