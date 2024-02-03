package tech.csm.service;

import java.util.List;

import tech.csm.entity.Village;

public interface VillageService {

	List<Village> getAllVillages();

	String saveVillage(Village v);

}
