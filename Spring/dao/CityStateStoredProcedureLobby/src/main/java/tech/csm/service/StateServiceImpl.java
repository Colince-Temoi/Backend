package tech.csm.service;

import java.util.ArrayList;
import java.util.List;

import tech.csm.dao.StateDao;
import tech.csm.dao.StateDaoImpl;
import tech.csm.entity.State;
import tech.csm.entity.StateVo;

public class StateServiceImpl implements StateService {

	private StateDao stateDao;

	public StateServiceImpl() {
		stateDao = new StateDaoImpl();
	}

	@Override
	public List<StateVo> getAllStates() {
		List<State> stateList = stateDao.getAllStates();

		/*
		 * for (State state : stateList) { System.out.println(state); }
		 */
//		Convert State Dto to Vo type
		List<StateVo> stateVoList = convertStateDtoToVo(stateList);

		return stateVoList;
	}

	private List<StateVo> convertStateDtoToVo(List<State> stateList) {

		List<StateVo> stateVoList = new ArrayList<>();

		for (State state : stateList) {
			StateVo stateVo = new StateVo();
			stateVo.setStateId(state.getStateId().toString());
			stateVo.setStateName(state.getStateName());

			stateVoList.add(stateVo);
		}
		return stateVoList;
	}

	@Override
	public StateVo getStateById(Integer id) {
		State state = stateDao.getStateById(id);

//		Convert State Dto to Vo type
		StateVo stateVo = convertStateDtoToVo(state);

		return stateVo;
	}

	private StateVo convertStateDtoToVo(State state) {

		StateVo stateVo = new StateVo();

		stateVo.setStateId(state.getStateId().toString());
		stateVo.setStateName(state.getStateName());

		return stateVo;
	}

}
