package tech.csm.service;
import java.util.List;

import tech.csm.domain.Location;

public interface LocationsService {

    List<Location> getAllLocations();
    Location getLocationById(Integer id);
	Integer saveLocation(Location location);
}