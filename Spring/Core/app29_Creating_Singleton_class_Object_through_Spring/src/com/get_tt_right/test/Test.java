package com.get_tt_right.test;

import java.util.Calendar;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.Singleton;

public class Test {

	public static void main(String[] args) {
//		Prepare Container
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		Access Container created beans
		Singleton singleton = (Singleton) context.getBean("singleton");
		Singleton singleton1 = (Singleton) context.getBean("singleton");
		
		Calendar calendar = (Calendar) context.getBean("calendar");
		Calendar calendar1 = (Calendar) context.getBean("calendar");
		
//		Singleton object test
		System.out.println(singleton==singleton1);
		System.out.println(calendar==calendar1);

//		Access CalendarFactory members
		System.out.println(calendar.getCalendarType());
		
		/*		In this way/approach we cannot instantiate Calendar class object --> Cannot instantiate the type Calendar
		Calendar c = new Calendar(); */
	}

}
/*NOTES:
 * In our jre, we have Calendar class.
 * Directly we can't be able to access calendar class constructor as shown above.
 * The Constructor is a private Constructor.
 * Then how to create it's object.
 * We've seen proof that Spring is able to create private constructor classes objects.
 * In Spring configuration file; if you configure Calendar class; from our knowledge Container should be able to create it's object? But it won't? haha
 * If you try to access the expected Container created object of Calendar class; an exception happilly you will get.
 * It's only through factory-method; getInstance() that we can create Calendar class object. See how in the Spring configuration file how to perform configurations the correct way.
 * */

