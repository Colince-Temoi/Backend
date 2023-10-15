package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.Car;

public class Test { 
	
	public static void main(String[] args) {
		
//		Prepare Container
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		Access Container created objects.
//		Car c = (Car) context.getBean("c");
		
//		Accessing static methods.
		Car.printCarName();
		
	}
}
