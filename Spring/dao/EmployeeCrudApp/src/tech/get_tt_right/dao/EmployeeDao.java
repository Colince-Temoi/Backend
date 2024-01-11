package tech.get_tt_right.dao;

import java.util.List;

import tech.get_tt_right.domain.Employee;

public interface EmployeeDao {

	String addEmployee(Employee employee);

	List<Employee> getAllEmployees();

	Employee getEmployeeById(int employeeId);

	String updateEmployee(Employee employeeToUpdate);

	String deleteEmployee(int deleteEmployeeId);

}
