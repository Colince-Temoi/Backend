package com.get_tt_right.test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.business.Bank;

public class Test {

	public static void main(String[] args) {
//		Prepare Container
		ConfigurableApplicationContext cap = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		Bank bproxy = (Bank) cap.getBean("bproxy");
		bproxy.deposit(1000, "CBA123");
		
		System.out.println("-------------------I don't want to execute services for findbal() method---------------------------");
		int bal = bproxy.findBal("CBA123");
		System.out.println(bal);
	}

}
