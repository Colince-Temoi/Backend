package tech.csm.dao;

import java.util.List;

import tech.csm.entity.State1;

public interface StateDao1 {

	List<State1> getAllStates();

	State1 getStateById(Integer id);

}
