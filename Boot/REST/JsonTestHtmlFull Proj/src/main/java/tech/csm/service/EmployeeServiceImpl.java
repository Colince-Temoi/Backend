package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.dao.EmployeeDao;
import tech.csm.domain.Employee;

@Service
public class EmployeeServiceImpl implements EmployeeSevice {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	public String saveEmployee(Employee employee) {
		return employeeDao.saveEmployee(employee);
	}

	@Override
	public List<Employee> findAllEmployees() {
		
		return employeeDao.findAllEmployees();
	}

}
