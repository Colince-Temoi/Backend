package tech.csm.dao;

import java.util.List;

import tech.csm.domain.Employee;

public interface EmployeeDao {
	
	List<Employee> getAllEmployees();
	Employee getEmployeeById(int id);

}
