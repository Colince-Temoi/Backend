package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.dao.Departmentdao;
import tech.csm.domain.Department;

@Service
public class DepartmentServiceImpl implements DepartmentService{
	
	@Autowired
	private Departmentdao departmentDao;

	@Override
	public List<Department> findAllDepartments() {
		List<Department> deptList = departmentDao.findAllDepartments();
		return deptList;
	}


}
