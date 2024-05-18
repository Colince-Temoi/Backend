package tech.csm.dao;
import java.util.List;

import tech.csm.domain.Location;

public interface LocationsDao {

    List<Location> getAllLocations();
    Location getLocationById(int id);
}