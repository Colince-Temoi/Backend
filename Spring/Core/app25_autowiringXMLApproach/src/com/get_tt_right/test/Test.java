package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.Bus;
import com.get_tt_right.beans.Car;

public class Test {
	/*
	 * For the xml file parameter, change that to see the other different types of
	   autowiring.
	 *Under source folder i have 4 .xml files; check them out.
	 */
	public static void main(String[] args) {
//		Prepare Container
		ApplicationContext context = new ClassPathXmlApplicationContext("autowiring_constructor.xml");
		
//		Access Container created objects.
//		Car c = (Car)context.getBean("c");
		Bus b = (Bus)context.getBean("b");
		
//		Access||invoke Car||Bus class members.
//		c.printData();
		b.printData();

	}

}
