package tech.csm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tech.csm.domain.Address;
import tech.csm.domain.Student;
import tech.csm.repo.AddressRepo;
import tech.csm.repo.StudentRepo;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepo studentRepo;
	
	@Autowired
	private AddressRepo addressRepo;

	@Override
	public String saveStudent(Student student) {
		studentRepo.save(student);
		
//		Manually save addresses
		 for (Address address : student.getAddresses()) { 
			 address.setStudent(student);
			 addressRepo.save(address);
		 }
		return "Student details saved successfully";
	}

}
