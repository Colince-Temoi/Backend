package tech.get_tt_right.service;

import java.util.List;

import tech.get_tt_right.domain.EmployeeVo;

public interface EmployeeService {

	String addEmployee(EmployeeVo employeeVo);

	EmployeeVo getEmployeeById(int employeeId);

	List<EmployeeVo> getAllEmployees();

	String updateEmployee(EmployeeVo updatedEmployee);

	String deleteEmployee(int deleteEmployeeId);

}
