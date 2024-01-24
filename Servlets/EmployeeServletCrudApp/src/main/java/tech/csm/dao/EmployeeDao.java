package tech.csm.dao;

import java.util.List;

import tech.csm.entity.Employees;

public interface EmployeeDao {

	List<Employees> getAllEmps();

	String saveEmp(Employees emp);

	String deleteEmpById(Integer eId);

	Employees getEmpById(Integer eId);

}
