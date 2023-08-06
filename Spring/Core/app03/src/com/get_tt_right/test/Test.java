package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.Employee;

public class Test {

	public static void main(String[] args) {
//		Prepare Container
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
		
		Employee emp = (Employee) context.getBean("hello");
		Employee emp1 = (Employee) context.getBean("empBean2");
		
		emp.getEmployeeDetails();
		emp1.getEmployeeDetails();
//For the below two lines of code, i just chose not to store the object into a variable unlike the above two lines of code. Otherwise it is just one and the same thing.
//For the above 2 lines of code, it is useful if you want to re-use the object in multiple places||times.
//For the below 2 lines of code, it is useful if you want to use the object only once.
		((Employee) context.getBean("empBean3")).getEmployeeDetails();
		((Employee) context.getBean("empBean6")).getEmployeeDetails();

//All the above references are pointing to the same object as can be proven below.
		System.out.println("----------------Proof for Singleton Scope-----------------------------");
		System.out.println(emp);
		System.out.println(emp1);
		System.out.println(((Employee) context.getBean("empBean3")));
		System.out.println(((Employee) context.getBean("empBean6")));		

	}

}
/*
NOTE: It is possible to get Bean objects on the basis of either identity||alias name(s). 
- Like this, we can utilize any based on our preference.
*/

