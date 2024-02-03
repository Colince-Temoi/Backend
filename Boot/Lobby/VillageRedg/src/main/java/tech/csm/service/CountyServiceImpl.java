package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.entity.County;
import tech.csm.repository.CountyRepo;

@Service
public class CountyServiceImpl implements CountyService {

	@Autowired
	private CountyRepo countyRepo;

	@Override
	public List<County> getAllCounties() {
//		Invoke CountyRepo behaviors	
//		JPA repository will provide implementations for this method at runtime
		return countyRepo.findAll();
	}
}
