package tech.csm.service;

import tech.csm.domain.EmpVO;

public interface EmpService {
	String addEmp(EmpVO emp);

	void showAllEmps();

	EmpVO deleteEmpById(Integer id);
}
