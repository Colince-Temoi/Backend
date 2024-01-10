package tech.get_tt_right.dao;

import java.util.List;

import tech.get_tt_right.domain.Department;

public interface DepartmentDao {

	List<Department> getAllDepartments();

	Department getDepartmentById(Integer id);

}
