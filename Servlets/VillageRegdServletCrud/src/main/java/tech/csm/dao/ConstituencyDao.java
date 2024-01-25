package tech.csm.dao;

import java.util.List;

import tech.csm.entity.Constituency;

public interface ConstituencyDao {

	List<Constituency> getConstituencieByCountyId(Integer countyId);

}
