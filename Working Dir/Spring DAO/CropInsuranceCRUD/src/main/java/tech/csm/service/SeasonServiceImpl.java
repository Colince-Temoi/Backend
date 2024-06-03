package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.model.Season;
import tech.csm.repo.SeasonRepo;

@Service
public class SeasonServiceImpl implements SeasonService {
	@Autowired
	private SeasonRepo seasonRepo;
	
	@Override
	public List<Season> getAllSeasons() {
		return seasonRepo.findAll();
	}

}
