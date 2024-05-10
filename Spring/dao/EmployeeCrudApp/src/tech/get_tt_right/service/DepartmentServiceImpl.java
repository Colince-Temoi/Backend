package tech.get_tt_right.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tech.get_tt_right.dao.DepartmentDao;
import tech.get_tt_right.dao.DepartmentDaoImpl;
import tech.get_tt_right.domain.Department;
import tech.get_tt_right.domain.DepartmentVo;

public class DepartmentServiceImpl implements DepartmentService {

//	Secondary dependencies
	DepartmentDao departmentDao;

	public DepartmentServiceImpl() {
		departmentDao = new DepartmentDaoImpl();
	}
	
	
	@Override
	public List<DepartmentVo> getAllDepartments() {
	    List<Department> departments = departmentDao.getAllDepartments();

	    // Convert Department DTOs to DepartmentVo objects
	    return departments.stream()
	            .map(dept -> {
	                DepartmentVo deptVo = new DepartmentVo();
	                deptVo.setDepartmentId(String.valueOf(dept.getDepartmentId())); 
	                deptVo.setDepartmentName(dept.getDepartmentName());
	                return deptVo;
	            })
	            .collect(Collectors.toList()); 
	}
	
	
	@Override
	public DepartmentVo getDepartmentById(String id) {
	    Integer departmentId = Integer.parseInt(id); // Assuming id is originally a String
	    Department department = departmentDao.getDepartmentById(departmentId);

	    if (department != null) {
	        DepartmentVo deptVo = new DepartmentVo();
	        deptVo.setDepartmentId(id);
	        deptVo.setDepartmentName(department.getDepartmentName());
	        return deptVo;
	    }

	    return null; // Department not found
	}
	
	

//	@Override
//	public List<DepartmentVo> getAllDepartments() {
//		List<DepartmentVo> departmentVos = new ArrayList<>();
//		List<Department> departments = departmentDao.getAllDepartments();
//
////		Convert each Department into DepartmentVo
//
//		if (departments != null) {
//			for (Department department : departments) {
//				DepartmentVo departmentVo = convertFromDtotoVo(department);
//				departmentVos.add(departmentVo);
//			}
//		} else {
//			return null; // No need to include this else part bcs if departments are not available; by
//							// default it will return null.
//		}
//
//		return departmentVos;
//	}

//	Conversion from Dto to Vo
	public DepartmentVo convertFromDtotoVo(Department department) {

//		Create a DepartmentVo object in which to store data after completion of the convertions.
		DepartmentVo departmentVo = new DepartmentVo();

		departmentVo.setDepartmentId(department.getDepartmentId().toString());
		departmentVo.setDepartmentName(department.getDepartmentName());

		return departmentVo;

	}

//	Conversion from Vo to Dto
	public Department convertFromVotoDto(DepartmentVo departmentVo) {

//		Create a DepartmentVo object in which to store data after completion of the convertions.
		Department department1 = new Department();

		department1.setDepartmentId(Integer.parseInt(departmentVo.getDepartmentId()));
		department1.setDepartmentName(departmentVo.getDepartmentName());

		return department1;

	}




//	@Override
//	public DepartmentVo getDepartmentById(String id) {
//		Department department = departmentDao.getDepartmentById(Integer.parseInt(id));
//		DepartmentVo departmentVo = convertFromDtotoVo(department);
//		return departmentVo;
//	}



}
