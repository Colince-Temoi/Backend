package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.Car;

public class Test {

	public static void main(String[] args) {

//		Prepare Container
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

//		Access bean objects from Container.
		Car c = (Car)context.getBean("c");

//		Access||invoke Car class members.
		c.printData();
	}

}
