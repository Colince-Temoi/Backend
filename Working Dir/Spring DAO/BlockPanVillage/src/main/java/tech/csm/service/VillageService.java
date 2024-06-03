package tech.csm.service;

import java.util.List;

import tech.csm.entity.Village;

public interface VillageService {
	String addVillage(Village village);

	List<Village> getAllVillages();

}
