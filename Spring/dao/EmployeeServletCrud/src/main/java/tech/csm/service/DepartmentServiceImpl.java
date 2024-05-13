package tech.csm.service;

import java.util.List;

import tech.csm.dao.DepartmentDao;
import tech.csm.dao.DepartmentDaoImpl;
import tech.csm.entity.Departments;

public class DepartmentServiceImpl implements DepartmentService {
	private DepartmentDao departmentDao;
	public DepartmentServiceImpl() {
		departmentDao=new DepartmentDaoImpl();
	}
	@Override
	public List<Departments> getAllDepartments() {
		
		return departmentDao.getAllDepartments();
	}

}
