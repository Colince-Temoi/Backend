package tech.csm.service;

import java.util.List;

import tech.csm.entity.Constituency;

public interface ConstituencyService {

	List<Constituency> getConstituencieByCountyId(Integer countyId);

}
