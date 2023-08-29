package com.get_tt_right.test;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.HelloBean;

/*By using ApplicationContext, just bean initialization we are able to get.
 * To get bean destruction  explicitly, context.registerShutdownHook we have to invoke.
 * This method will destroy bean objects explicitly. But this method is only available in
 *   AbstractApplicationContext a sub-interface of ApplicationContext.
 * NOTE: Whenever we are calling this, context.registerShutDownHook, all the beans whatever are their in the container, those are going to be destroyed.
 *       It is something like terminating the container forcibly, hence all the beans are going to destroyed.
 * registerShutdownHook() is general information given to the container to destroy bean objects during de-instantiation.
 * Q. Is there a way of destroying respective single bean objects?
 * Ans. You may think of a core Java concept like, assign the bean object a null value, then invoke System.gc but definately this won't work in Spring applications.
 *    System.gc we have invoked that for de-instantiation purpose, by this you may think that bean object eligible for garbage collection[Those assigned null values] will be destroyed,
 *    but, NO!! REASON:
 *    System.gc can only be used to destroy objects which are inside the Heap memory, but actually, bean objects in Spring applications are managed by Container which from the demo, it will clearly not allow
 *    the Garbage collector to destroy these objects.
 *    If it were possible to destroy bean objects through the garbage collector, then where is the control for the Container?? Where is the security for the beans which are managed by the container?? Food for thought.
 * Like this you need to understand that we are beyond the JVM and talking about the Container. This is the Corner stone in Spring applications.
 * Container might be using JVM but in its controlled fashion, only as per the Spring rules and regulations.
 * When ApplicationContext wants to destroy bean objects, then only at that time it will destroy, Otherwise bean objects won't be destroyed here.
 * 
 * */

public class Test {

	public static void main(String[] args) {

//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		getting bean object
		HelloBean hellobean = (HelloBean) context.getBean("helloBean");
//		Accessing the bean object method(s)
		System.out.println(hellobean.sayHello());
		
//		context.registerShutdownHook();
		
//		hellobean=null;
//		System.gc();

		context.registerShutdownHook();
	}

}
