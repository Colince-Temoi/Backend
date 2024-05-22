package tech.csm.service;

import jakarta.validation.Valid;
import tech.csm.domain.Region;

public interface RegionService {

//	Integer saveAddress(Region region);
	
	public Integer saveAddress(Region region);
	
	Region getRegionById(Integer regionId);

	public Integer updateAddress(@Valid Region region);

	public Integer deleteAddress(Integer id);

//	Region getRegionById(Integer savedRegionId);

}
