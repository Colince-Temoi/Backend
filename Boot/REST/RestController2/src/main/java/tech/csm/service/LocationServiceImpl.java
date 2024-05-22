package tech.csm.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.dao.LocationsDao;
import tech.csm.domain.Location;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationsService {

	@Autowired
    private LocationsDao locationsDao;

    @Override
    public List<Location> getAllLocations() {
    	System.out.println("Inside get all locations service method");
        return locationsDao.getAllLocations();
    }
    
    @Override
    public Location getLocationById(Integer id) {
    	System.out.println("Inside get location by id service method");
        return locationsDao.getLocationById(id);
    }

	@Override
	public Integer saveLocation(Location location) {
		// TODO Auto-generated method stub
		return null;
	}
}