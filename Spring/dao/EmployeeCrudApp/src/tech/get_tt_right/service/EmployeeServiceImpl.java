package tech.get_tt_right.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		employee.setEmpId(Integer.parseInt(evo.getEmpId()));
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
		// Convert EmployeeVo to Employee
		Employee employeeToUpdate = convertFromVoToDto(updatedEmployee);

		// Invoke DAO method to update the employee
		String msg = employeeDao.updateEmployee(employeeToUpdate);

		return msg;
	}

	@Override
	public String deleteEmployee(int deleteEmployeeId) {
	    // Call DAO method to delete Employee by ID
	    String deleteMessage = employeeDao.deleteEmployee(deleteEmployeeId);

	    return deleteMessage;
	}


}
