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
	public Integer saveEmployee(Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Employee> getAllEmployees() {
//		System.out.println("Excecuting!!");
		return employeeDao.getAllEmployees();
	}
	
	public Employee getEmployeeById(int id) {
        return employeeDao.getEmployeeById(id);
    }

}
