package tech.csm.service;

import java.util.ArrayList;

import tech.csm.dao.EmpDao;
import tech.csm.dao.EmpDaoImpl;
import tech.csm.domain.EmpDTO;
import tech.csm.domain.EmpVO;

public class EmpServiceImpl implements EmpService {

	private EmpDao empDao;
	public EmpServiceImpl() {
		empDao=new EmpDaoImpl();
	}
	
	@Override
	public String addEmp(EmpVO empVO) {
		EmpDTO empDTO=new EmpDTO();
		empDTO.setEmpId(Integer.parseInt(empVO.getEmpId()));
		empDTO.setName(empVO.getName());
		empDTO.setSal(Double.parseDouble(empVO.getSal()));
		
		return empDao.addEmp(empDTO);
	}

	@Override
	public void showAllEmps() {
		ArrayList<EmpDTO> empList=empDao.showAllEmps();
		for(EmpDTO x:empList)
			System.out.println(x);
		
	}

	@Override
	public EmpVO deleteEmpById(Integer id) {
		EmpDTO empDTO=empDao.deleteEmpById(id);
		EmpVO empVO=null;
		if(empDTO!=null) {
			empVO=new EmpVO();
			empVO.setEmpId(empDTO.getEmpId().toString());
			empVO.setName(empDTO.getName());
			empVO.setSal(empDTO.getSal().toString());
			return empVO;
		}
		 return null;
	}

}
