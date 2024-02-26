package com.get_tt_right.main;

import java.sql.SQLException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.dao.StudentDaoImpl;
import com.get_tt_right.model.StudentModel;

public class Main {

	public static void main(String[] args) {

		ConfigurableApplicationContext cap = new ClassPathXmlApplicationContext("applicationcontext.xml");

		StudentDaoImpl studentDaoImpl = (StudentDaoImpl) cap.getBean("studentDaoImpl");
		StudentModel studentModel = (StudentModel) cap.getBean("studentdaomodel");

		try {
			Integer res = studentDaoImpl.save(studentModel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
