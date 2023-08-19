package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.beans.HelloBean;
import com.get_tt_right.scopes.ThreadScope;
/*Thread Scope
 * In this program, I want to keep this HelloBean object in thread scope.
 * Scope related implementation we have to provide.
 * Steps
 * -----
 * 1. Prepare User-defined scope class: CustomThreadLocal
 *    - Do this in separate package: scopes package.
 *    - Must extend predefined class: ThreadLocal
 * */
/* Before creating any User defined thread, only main thread is executing.
 * With respect to main thread, a local will be created.
 * Inside this local we are able to keep object and we are able to retrieve that object from the local.
 * With respect to main thread, only one bean object is going to be created. Even though we are||may call getBean method n times, we will only get one object reference.
 * 
 * 2 objects we are creating: helloBean and helloBean1.
 * We are then going ahead and using these references to invoke HelloBean class sayHello() method as well as printing their reference values.
 * Now here, main thread is available and with respect to that only one bean object is going to be created even though we are calling multiple times context.getBean() method.
 * 
 * Even if we call context.getBean() method 100 times, we are going to get the same object.
 * */

/*ThreasScope object I want to get as well here. REASON:
 * I want to invoke remove() method as well.
 * helloBean object i want remove from scope.
 * After that, once again I want to get helloBean object. Will it be same as what we removed previously??.
 * Definitely NO!!. Different reference we will get. REASON:
 * Existing bean object from scope(Map) we removed their, so this is a new object. 
 * This you can clearly see from the output, as instantiation is happening once more.
 * Like this you can add and remove bean objects from scope as much as you want to internalize the concept.
 * 
 * By this program, we can come up with a conclusion that, with respect to thread a separate bean object is created and
 * for up to a particular thread even if you are calling 100 times getBean method, all that will refer to the single bean object.
 * 
 * This was the best example to demonstrate thread scope(Nothing but custom scope implementation).
 * */

/*Creating other User Defined threads
 * We are going to create them by implementing Runnable interface.
 * Runnable interface is a Functional interface. Visit core Java concept for more details.
 * A functional interface us one that has only one rule||abstract method.
 * So runnable interface has this abstract method called: run method.
 * 
 * From core Java, we know that, to implement interfaces, several approaches are available.
 *  1. By using implements key word.[OLD]
 *  2. By using anonymous inner class implementation. Recommended for Non-Functional interfaces.
 *  3. By using lambda Expressions. Suitable and recommended for Functional interfaces. REASON: Find out from core java concepts in notes.
 *  4. By using Method reference.
 * 
 * In this example, I am going to utilize: anonymous inner class && lambda expression to create our user defined threads.
 * */

public class Test {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		HelloBean helloBean=(HelloBean)context.getBean("helloBean");
		HelloBean helloBean1=(HelloBean)context.getBean("helloBean");
		
		System.out.println(helloBean.sayHello());
		System.out.println(helloBean1.sayHello());
		
		System.out.println("Main Thread: "+helloBean);
		System.out.println("Main Thread: "+helloBean1);
		
//		ThreadScope threadScope=(ThreadScope)context.getBean("threadScope");
//	
//		HelloBean helloBean2=(HelloBean)threadScope.remove("helloBean");
//		System.out.println(helloBean2);
//		
//		HelloBean helloBean3=(HelloBean)context.getBean("helloBean");
//		System.out.println(helloBean3);
		
//		Creating User-Defined thread: thread0
		Runnable r= new Runnable() {
			
			@Override
			public void run() {
				HelloBean helloBean4= (HelloBean)context.getBean("helloBean");
				HelloBean helloBean5=(HelloBean)context.getBean("helloBean");
				System.out.println("User Thread1: "+helloBean4);
				System.out.println("User Thread1: "+helloBean5);
				
			}
		};
		Thread t=new Thread(r);
		t.start();
		
//      Creating another User-defined thread using lambda expression: thread1
		Thread t1=new Thread(()->{
			HelloBean helloBean7=(HelloBean)context.getBean("helloBean");
			HelloBean helloBean8=(HelloBean)context.getBean("helloBean");
			System.out.println("User Thread2: "+helloBean7);
			System.out.println("User Thread2: "+helloBean8);
		});
		t1.start();
	}

}
