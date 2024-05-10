package tech.csm.dao;

import java.util.List;

import tech.csm.entity.State;

public interface StateDao {

	List<State> getAllStates();

	State getStateById(Integer id);

}
