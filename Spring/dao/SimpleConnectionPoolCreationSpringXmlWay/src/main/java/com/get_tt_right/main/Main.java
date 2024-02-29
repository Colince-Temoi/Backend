package com.get_tt_right.main;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.dao.StudentDaoImpl;
import com.get_tt_right.model.StudentModel;
import com.get_tt_right.service.StudentServiceImpl;

public class Main {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext cap = new ClassPathXmlApplicationContext("applicationcontext.xml");

		StudentServiceImpl studentServiceImpl = (StudentServiceImpl) cap.getBean("studentServiceImpl");
		StudentModel studentModel = (StudentModel) cap.getBean("studentdaomodel");

		studentServiceImpl.save(studentModel);
//		studentDaoImpl.update(studentModel);
//		studentDaoImpl.delete(studentModel);
//		studentDaoImpl.findByPk(studentModel.getSId());
//		studentDaoImpl.findByEmail(studentModel.getEmail());
//		studentDaoImpl.findAllStudents();
	}
}
