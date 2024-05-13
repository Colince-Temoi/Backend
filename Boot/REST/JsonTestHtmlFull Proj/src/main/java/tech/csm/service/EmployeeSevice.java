package tech.csm.service;

import java.util.List;

import tech.csm.domain.Employee;

public interface EmployeeSevice {

	String saveEmployee(Employee employee);

	List<Employee> findAllEmployees();

}
