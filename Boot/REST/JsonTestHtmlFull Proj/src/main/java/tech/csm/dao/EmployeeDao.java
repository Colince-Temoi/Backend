package tech.csm.dao;

import java.util.List;

import tech.csm.domain.Employee;

public interface EmployeeDao {

	String saveEmployee(Employee employee);

	List<Employee> findAllEmployees();

}
