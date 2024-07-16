package tech.csm.service;

import java.util.ArrayList;
import java.util.List;

import tech.csm.dao.StateDao1;
import tech.csm.dao.StateDaoImpl1;
import tech.csm.entity.State1;
import tech.csm.entity.StateVo1;

public class StateServiceImpl1 implements StateService1 {

	private StateDao1 stateDao;

	public StateServiceImpl1() {
		stateDao = new StateDaoImpl1();
	}

	@Override
	public List<StateVo1> getAllStates() {
		List<State1> stateList = stateDao.getAllStates();

		/*
		 * for (State state : stateList) { System.out.println(state); }
		 */
//		Convert State Dto to Vo type
		List<StateVo1> stateVoList = convertStateDtoToVo(stateList);

		return stateVoList;
	}

	private List<StateVo1> convertStateDtoToVo(List<State1> stateList) {

		List<StateVo1> stateVoList = new ArrayList<>();

		for (State1 state : stateList) {
			StateVo1 stateVo = new StateVo1();
			stateVo.setStateId(state.getStateId().toString());
			stateVo.setStateName(state.getStateName());

			stateVoList.add(stateVo);
		}
		return stateVoList;
	}

	@Override
	public StateVo1 getStateById(Integer id) {
		State1 state = stateDao.getStateById(id);

//		Convert State Dto to Vo type
		StateVo1 stateVo = convertStateDtoToVo(state);

		return stateVo;
	}

	private StateVo1 convertStateDtoToVo(State1 state) {

		StateVo1 stateVo = new StateVo1();

		stateVo.setStateId(state.getStateId().toString());
		stateVo.setStateName(state.getStateName());

		return stateVo;
	}

}
