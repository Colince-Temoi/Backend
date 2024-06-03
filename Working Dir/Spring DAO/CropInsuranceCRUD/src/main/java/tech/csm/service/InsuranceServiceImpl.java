package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tech.csm.model.Crop;
import tech.csm.model.Insurance;
import tech.csm.repo.CropRepo;
import tech.csm.repo.InsuranceRepo;

@Service
@Transactional
public class InsuranceServiceImpl implements InsuranceService {

	@Autowired
	private InsuranceRepo insuranceRepo;
	
	@Override
	public Insurance saveInsurance(Insurance insurance) {
		
		
		Insurance i=insuranceRepo.save(insurance);		
		return i;
	}

	@Override
	public List<Insurance> getAllInsurance() {
		return insuranceRepo.findAll();
	}

	@Override
	public String deleteInsuranceById(Integer insuranceId) {
		Insurance insurance=insuranceRepo.findById(insuranceId).get();
		insuranceRepo.delete(insurance);
		return "1 insurance deleted";
	}

	@Override
	public Insurance getInsuranceById(Integer insuranceId) {
		
		return insuranceRepo.findById(insuranceId).get();
	}

	@Override
	public boolean aadhaarNoexistsByCropId(Integer cropId, Integer aadhaarNo) {
		return insuranceRepo.existsByCrop_CropIdAndAadhaarNo(cropId, aadhaarNo);
	}
	
	

}
