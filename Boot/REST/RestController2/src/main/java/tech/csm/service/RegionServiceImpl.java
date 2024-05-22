package tech.csm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.dao.RegionDao;
import tech.csm.domain.Region;

@Service
public class RegionServiceImpl implements RegionService {

	 @Autowired
	    private RegionDao regionDao;

	    @Override
	    public Region getRegionById(Integer regionId) {
	        return regionDao.getRegionById(regionId);
	    }

	    @Override
	    public Integer saveAddress(Region region) {
	    	System.out.println("Exceuting  save address Method");
	    	Integer regionId = regionDao.saveAddress(region);
	        return regionId;
	    }
	    
	    @Override
	    public Integer updateAddress(Region region) {
	    	System.out.println("Exceuting  update address Method");
	        return regionDao.updateAddress(region);
	    }
	    
	    @Override
	    public Integer deleteAddress(Integer regionId) {
	    	System.out.println("Exceuting  delete address Method");
	        return regionDao.deleteAddress(regionId);
	    }

}
