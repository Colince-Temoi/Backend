package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.domain.State;
import tech.csm.repo.StateDao;

@Service
public class StateServiceImpl implements StateService {

	@Autowired
	private StateDao stateDao;
	
	
	@Override
	public List<State> findAllStates() {
		
		return stateDao.findAll();
	}

}
