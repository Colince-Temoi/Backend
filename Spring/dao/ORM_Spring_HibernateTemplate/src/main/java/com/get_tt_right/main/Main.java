package com.get_tt_right.main;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import com.get_tt_right.dao.StudentDaoImpl;
import com.get_tt_right.model.StudentModel;
import com.get_tt_right.service.StudentServiceImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Main {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext cap = new ClassPathXmlApplicationContext("applicationcontext.xml");

		StudentServiceImpl studentServiceImpl = (StudentServiceImpl) cap.getBean("studentServiceImpl");

		// Creating an object of StudentModel
		StudentModel student = new StudentModel();

		// Populating the object with dummy data using setter methods
		student.setId(9);
		student.setName("John Doe");
		student.setEmail("john@example.com");
		student.setAddress("219");

		studentServiceImpl.save(student);
//		studentDaoImpl.update(studentModel);
//		studentDaoImpl.delete(studentModel);
//		studentDaoImpl.findByPk(studentModel.getSId());
//		studentDaoImpl.findByEmail(studentModel.getEmail());
//		studentDaoImpl.findAllStudents();
	}
}
