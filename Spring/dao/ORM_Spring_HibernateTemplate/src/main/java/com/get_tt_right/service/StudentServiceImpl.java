package com.get_tt_right.service;

import com.get_tt_right.dao.StudentDao;
import com.get_tt_right.model.StudentModel;

import lombok.Setter;

/*IOC
 * Injecting into interface reference interface implementation class reference.
 * In future if you don't want a particular interface implementation class, you can change it to some other interface implementaion class and it will not affect your business
 * Reason: Business is not tightly coupled to any interface implementation class.
 * Business here is having interface dependency. Into this interface any implementation we can inject. The implementation could be through:
 * Plain Jdbc like we have done || Hibernate ||Jpa || Spring Jdbc
 * This is what exactly Inversion of Controlling is. Having the capabilities to make changes anytime as you wish. PnP you can call it.
 * */

@Setter
public class StudentServiceImpl implements StudentService {

	
	
	private StudentDao studentDao;

	public String save(StudentModel student)throws Exception {
		Integer ra = studentDao.save(student);
		System.out.println(ra + " Student data saved successfully.");
		return ra + " Student data saved successfully.";
	}

}
