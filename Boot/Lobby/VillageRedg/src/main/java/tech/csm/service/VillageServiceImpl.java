package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.entity.Village;
import tech.csm.repository.VillageRepo;

@Service
public class VillageServiceImpl implements VillageService {

	@Autowired
	private VillageRepo villageRepo;
	
	@Override
	public List<Village> getAllVillages() {
		return villageRepo.findAll();
	}
	
	@Override
	public String saveVillage(Village v) {
   Village sv = villageRepo.save(v);
		return "Vilage saved with id: "+sv.getVillageId();
	}

}
