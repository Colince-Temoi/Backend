package tech.csm.service;

import java.util.List;

import tech.csm.entity.StateVo1;

public interface StateService1 {

	List<StateVo1> getAllStates();

	StateVo1 getStateById(Integer id);

}
