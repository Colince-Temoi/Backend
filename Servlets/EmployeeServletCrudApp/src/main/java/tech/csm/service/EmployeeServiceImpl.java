package tech.csm.service;

import java.util.List;

import tech.csm.dao.EmployeeDao;
import tech.csm.dao.EmployeeDaoImpl;
import tech.csm.entity.Employees;

public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeDao employeeDao;

	public EmployeeServiceImpl() {
		employeeDao = new EmployeeDaoImpl();
	}

	@Override
	public List<Employees> getAllEmployees() {
		return employeeDao.getAllEmps();
	}

	@Override
	public String saveEmp(Employees emp) {
		return employeeDao.saveEmp(emp);
	}

	@Override
	public String deleteEmpById(Integer eId) {
		return employeeDao.deleteEmpById(eId);
	}

}
