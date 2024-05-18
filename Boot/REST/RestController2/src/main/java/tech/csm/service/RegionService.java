package tech.csm.service;

import tech.csm.domain.Region;

public interface RegionService {

//	Integer saveAddress(Region region);
	
	public Integer saveAddress(Region region);
	
	Region getRegionById(Integer regionId);

//	Region getRegionById(Integer savedRegionId);

}
