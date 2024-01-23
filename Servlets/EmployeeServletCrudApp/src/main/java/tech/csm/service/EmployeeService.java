package tech.csm.service;

import java.util.List;

import tech.csm.entity.Employees;

public interface EmployeeService {

	List<Employees> getAllEmployees();

	String saveEmp(Employees emp);

}
