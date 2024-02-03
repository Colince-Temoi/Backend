package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.entity.Constituency;
import tech.csm.repository.ConstituencyRepo;

@Service
public class ConstituencyServiceImpl implements ConstituencyService {

	@Autowired
	private ConstituencyRepo constituencyRepo;

	@Override
	public List<Constituency> getAllConstituencies(Integer cId) {
		return constituencyRepo.findByCounty_countyId(cId);
	}

}
