package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.model.Crop;
import tech.csm.repo.CropRepo;

@Service
public class CropServiceImpl implements CropService {

	@Autowired
	private CropRepo cropRepo;
	
	@Override
	public List<Crop> getAllCrops() {
		return cropRepo.findAll();
	}

	@Override
	public Crop getCropById(Integer cropId) {
		return cropRepo.findById(cropId).get();
	}

	@Override
	public List<Crop> getCropBySeasonId(Integer seasonId) {
		return cropRepo.findBySeasonSeasonId(seasonId);
	}

	
}
