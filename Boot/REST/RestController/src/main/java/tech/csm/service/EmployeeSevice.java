package tech.csm.service;

import java.util.List;

import tech.csm.domain.Employee;

public interface EmployeeSevice {

	Integer saveEmployee(Employee employee);	
	List<Employee> getAllEmployees();
	 Employee getEmployeeById(int id);
	

}
