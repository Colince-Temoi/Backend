package tech.csm.service;

import java.util.List;

import tech.csm.entity.Employees;

public interface EmployeeService {

	String saveEmp(Employees emp);

	List<Employees> getAllEmps();

	String deleteEmpById(Integer eId);

	Employees getEmpById(Integer eId);

}
