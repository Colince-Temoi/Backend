package tech.csm.service;

import java.util.List;

import tech.csm.entity.StateVo;

public interface StateService {

	List<StateVo> getAllStates();

	StateVo getStateById(Integer id);

}
