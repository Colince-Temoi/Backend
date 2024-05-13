package tech.csm.dao;

import java.util.List;

import tech.csm.entity.Employees;

public interface EmployeeDao {

	String saveEmp(Employees emp);

	List<Employees> getAllEmps();

	String deleteEmpById(Integer eId);

	Employees getEmpById(Integer eId);

}
