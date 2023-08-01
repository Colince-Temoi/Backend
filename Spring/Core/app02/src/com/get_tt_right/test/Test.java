package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.HelloBean;

public class Test {

	public static void main(String[] args) {
	
		// create and configure beans||Activating ApplicationContext Container
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		// retrieve configured instance
		HelloBean hb = (HelloBean)context.getBean("helloBean");

		//	HardCoding is a bad coding practice.	
//		hb.setName("TMI");
		
		// use configured instance
		System.out.println(hb.sayHello());

	}

}
/* Do not set data through Java application.[Hard Cording]
 * For changing data-set that through the respective Spring Configuration file [applicationContext.xml]
 * 
 * */
