package tech.get_tt_right.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.get_tt_right.dao.EmployeeDao;
import tech.get_tt_right.dao.EmployeeDaoImpl;
import tech.get_tt_right.domain.Department;
import tech.get_tt_right.domain.DepartmentVo;
import tech.get_tt_right.domain.Employee;
import tech.get_tt_right.domain.EmployeeVo;

public class EmployeeServiceImpl implements EmployeeService {

//	Secondary Dependency
	private Employee employee;
	private EmployeeDao employeeDao;

	public EmployeeServiceImpl() {
		employee = new Employee();
		employeeDao = new EmployeeDaoImpl();

	}

	@Override
	public String addEmployee(EmployeeVo employeeVo) {
//		Convertions from Vo to Dto type
		Employee employee = convertFromVoToDto(employeeVo);

//		Invoke Dao layer method to perform the persistance.
		String msg = employeeDao.addEmployee(employee);

		return msg;
	}

	public Employee convertFromVoToDto(EmployeeVo evo) {

//		Conversion from Vo to Dto
//		employee.setEmpId(Integer.parseInt(evo.getEmpId()));
		employee.setName(evo.getName());
		employee.setSalary(Double.parseDouble(evo.getSalary()));

		try {
			employee.setHire_date(new SimpleDateFormat("dd/MM/yyyy").parse(evo.getHire_date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		employee.setIsDeleted(evo.getIsDeleted());

//		Convert DepartmentVo object into Department dto type

//		Create a DepartmentVo object in which to store data after completion of the convertions.
		Department department1 = new Department();

		department1.setDepartmentId(Integer.parseInt(evo.getDepartment().getDepartmentId()));
		department1.setDepartmentName(evo.getDepartment().getDepartmentName());

		employee.setDepartment(department1);

		return employee;

	}

	// Convert Employee to EmployeeVo
	private EmployeeVo convertToEmployeeVo(Employee employee) {
		EmployeeVo employeeVo = new EmployeeVo();
		employeeVo.setEmpId(String.valueOf(employee.getEmpId()));
		employeeVo.setName(employee.getName());
		employeeVo.setSalary(String.valueOf(employee.getSalary()));

		// Format the Date as a String
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		employeeVo.setHire_date(dateFormat.format(employee.getHire_date()));

		// EmployeeVo has DepartmentVo type reference and I need to only set the id to
		// DepartmentVo
		// Set department ID to DepartmentVo
		DepartmentVo departmentVo = new DepartmentVo();
		departmentVo.setDepartmentId(String.valueOf(employee.getDepartment().getDepartmentId()));
		employeeVo.setDepartment(departmentVo);

		// Additional mappings if needed

		return employeeVo;

	}

	@Override
	public EmployeeVo getEmployeeById(int employeeId) {
		// Call DAO method to retrieve Employee by ID
		Employee employee = employeeDao.getEmployeeById(employeeId);
		EmployeeVo employeeVo = null;

		// Check if the employee is found
		if (employee != null) {
			// Convert Employee to EmployeeVo
			employeeVo = convertToEmployeeVo(employee);
		}
		return employeeVo;
	}

	@Override
	public List<EmployeeVo> getAllEmployees() {
		List<Employee> employees = employeeDao.getAllEmployees();
		List<EmployeeVo> employeeVos = new ArrayList<>();
		// Invoke a function that converts from Dto to Vo type
		for (Employee employee : employees) {
			EmployeeVo employeeVo = convertToEmployeeVo(employee);
			employeeVos.add(employeeVo);
		}

		// Return the list of Vo types to the caller.
		return employeeVos;

	}

	@Override
	public String updateEmployee(EmployeeVo updatedEmployee) {
	    // Check if the employee with the given ID exists and is not soft-deleted
	    Employee existingEmployee = employeeDao.getEmployeeById(Integer.parseInt(updatedEmployee.getEmpId()));
	    if (existingEmployee != null && !"YES".equals(existingEmployee.getIsDeleted())) {
	        // Employee with the given ID is not soft-deleted
	        // Convert EmployeeVo to Employee

//	    	Converting id from inside this method
	    	employee.setEmpId(Integer.parseInt(updatedEmployee.getEmpId()));
//	    	The rest other properties will be converted from the method: convertFromVoToDto
	        Employee employeeToUpdate = convertFromVoToDto(updatedEmployee);

	        // Invoke DAO method to update the employee
	        String msg = employeeDao.updateEmployee(employeeToUpdate);

	        return msg;
	    } else {
	        return "Failed to update employee. Employee not found or is soft-deleted.";
	    }
	}


	@Override
	public String deleteEmployee(int deleteEmployeeId) {
	    // Call DAO method to delete Employee by ID
	    String deleteMessage = employeeDao.deleteEmployee(deleteEmployeeId);

	    return deleteMessage;
	}
	@Override
	public String getReportBySalary(double salary) {
	    List<Employee> employees = employeeDao.getEmployeesBySalary(salary);

	    if (!employees.isEmpty()) {
	        StringBuilder report = new StringBuilder("Salary Report for Employees with Salary >= " + salary + ":\n\n");
	        for (Employee employee : employees) {
	            report.append("Employee ID: ").append(employee.getEmpId()).append(", ");
	            report.append("Name: ").append(employee.getName()).append(", ");
	            report.append("Salary: ").append(employee.getSalary()).append("\n");
	        }
	        return report.toString();
	    } else {
	        return "No employees found with salary >= " + salary;
	    }
	}

	@Override
	public String getReportByHireDate(String hireDate) {
	    try {
	        Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse(hireDate);
	        List<Employee> employees = employeeDao.getEmployeesByHireDate(formattedDate);

	        if (!employees.isEmpty()) {
	            StringBuilder report = new StringBuilder("Hire Date Report for Employees hired on >= " + hireDate + ":\n\n");
	            for (Employee employee : employees) {
	                report.append("Employee ID: ").append(employee.getEmpId()).append(", ");
	                report.append("Name: ").append(employee.getName()).append(", ");
	                report.append("Hire Date: ").append(employee.getHire_date()).append("\n");
	            }
	            return report.toString();
	        } else {
	            return "No employees found hired on " + hireDate;
	        }
	    } catch (ParseException e) {
	        return "Invalid date format. Please use dd/MM/yyyy.";
	    }
	}

	@Override
	public String getReportByDepartment(String departmentId) {
	    try {
	        int deptId = Integer.parseInt(departmentId);
	        List<Employee> employees = employeeDao.getEmployeesByDepartment(deptId);

	        if (!employees.isEmpty()) {
	            StringBuilder report = new StringBuilder("Department Report for Employees in Department ID " + departmentId + ":\n\n");
	            for (Employee employee : employees) {
	                report.append("Employee ID: ").append(employee.getEmpId()).append(", ");
	                report.append("Name: ").append(employee.getName()).append(", ");
	                report.append("Department ID: ").append(employee.getDepartment().getDepartmentId()).append("\n");
	            }
	            return report.toString();
	        } else {
	            return "No employees found in Department ID " + departmentId;
	        }
	    } catch (NumberFormatException e) {
	        return "Invalid department ID format. Please provide a valid integer.";
	    }
	}



}
