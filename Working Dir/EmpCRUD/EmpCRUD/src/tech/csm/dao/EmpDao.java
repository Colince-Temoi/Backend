package tech.csm.dao;

import java.util.ArrayList;

import tech.csm.domain.EmpDTO;

public interface EmpDao {
	String addEmp(EmpDTO empDTO);

	ArrayList<EmpDTO> showAllEmps();

	EmpDTO deleteEmpById(Integer id);
}
