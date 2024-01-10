package tech.get_tt_right.service;

import java.util.List;

import tech.get_tt_right.domain.DepartmentVo;

public interface DepartmentService {

	List<DepartmentVo> getAllDepartments();

	DepartmentVo getDepartmentById(String id);

}
