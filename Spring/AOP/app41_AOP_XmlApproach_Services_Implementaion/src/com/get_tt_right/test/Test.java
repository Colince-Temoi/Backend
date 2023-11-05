package com.get_tt_right.test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.business.Bank;

public class Test {

	public static void main(String[] args) {
//		Prepare Container
		ConfigurableApplicationContext cap = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		Bank b = (Bank) cap.getBean("bproxy");
		b.deposit(1000, "CBA123");
	}

}
