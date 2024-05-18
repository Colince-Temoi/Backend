package tech.csm.dao;

import tech.csm.domain.Region;

public interface RegionDao {
    Region getRegionById(Integer regionId);
    public Integer saveAddress(Region region);
}