package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.DAO;

public class Test {

	public static void main(String[] args) throws Exception {
//		Starting Container
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

//Accessing Bean objects from container
		DAO dao = (DAO) context.getBean("dao");
		dao.printConnection();

	}

}
