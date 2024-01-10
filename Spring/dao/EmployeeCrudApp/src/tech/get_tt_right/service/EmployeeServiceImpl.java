package tech.get_tt_right.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import tech.get_tt_right.dao.EmployeeDao;
import tech.get_tt_right.dao.EmployeeDaoImpl;
import tech.get_tt_right.domain.Department;
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

		employee.setName(evo.getName());
		employee.setSalary(Double.parseDouble(evo.getSalary()));

		try {
			employee.setHire_date(new SimpleDateFormat("dd/MM/yyyy").parse(evo.getHire_date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		Convert DepartmentVo object into Department dto type

//		Create a DepartmentVo object in which to store data after completion of the convertions.
		Department department1 = new Department();

		department1.setDepartmentId(Integer.parseInt(evo.getDepartment().getDepartmentId()));
		department1.setDepartmentName(evo.getDepartment().getDepartmentName());

		employee.setDepartment(department1);

		return employee;

	}
}
