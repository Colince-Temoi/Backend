package tech.csm.service;

import java.util.List;

import tech.csm.model.Crop;

public interface CropService {

	List<Crop> getAllCrops();

	Crop getCropById(Integer cropId);

	List<Crop> getCropBySeasonId(Integer seasonId);

}
