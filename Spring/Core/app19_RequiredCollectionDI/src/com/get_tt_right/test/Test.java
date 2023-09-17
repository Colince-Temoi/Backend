package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.HelloBean;

public class Test {

	public static void main(String[] args) {
// Starting Container
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//Accessing beans from Container.
		HelloBean hb = (HelloBean) context.getBean("helloBean");
// Using/Invoking bean methods||data
		hb.printData();
		

	}

}
