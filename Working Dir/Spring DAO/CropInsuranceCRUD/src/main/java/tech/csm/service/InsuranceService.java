package tech.csm.service;

import java.util.List;

import tech.csm.model.Insurance;

public interface InsuranceService {

	Insurance saveInsurance(Insurance insurance);

	List<Insurance> getAllInsurance();

	String deleteInsuranceById(Integer insuranceId);

	Insurance getInsuranceById(Integer insuranceId);
	
	boolean aadhaarNoexistsByCropId(Integer cropId, Integer aadhaarNo);

}
