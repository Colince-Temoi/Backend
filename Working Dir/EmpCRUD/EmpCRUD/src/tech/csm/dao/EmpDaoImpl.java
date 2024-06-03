package tech.csm.dao;

import java.util.ArrayList;

import tech.csm.domain.EmpDTO;

public class EmpDaoImpl implements EmpDao {

	private ArrayList<EmpDTO> empList;
	
	public EmpDaoImpl() {
		empList=new ArrayList<>();
	}
	
	@Override
	public String addEmp(EmpDTO empDTO) {
		empList.add(empDTO);
		return "1 emp added";
	}

	@Override
	public ArrayList<EmpDTO> showAllEmps() {
		
		return empList;
	}

	@Override
	public EmpDTO deleteEmpById(Integer id) {
		EmpDTO t=null;
		for(int i=0;i<empList.size();i++) {
			if(empList.get(i).getEmpId().intValue()==id.intValue()) {
				t=empList.get(i);
				empList.remove(i);				
			}
		}
		return t;
	}

}
