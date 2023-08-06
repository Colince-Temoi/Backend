package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.Employee;

public class Test {

	public static void main(String[] args) {
//		Prepare Container
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.println("----------------Proof for Prototype scope-----------------");
		System.out.println(context.getBean("empBean"));
		System.out.println(context.getBean("empBean"));
		

	}

}

