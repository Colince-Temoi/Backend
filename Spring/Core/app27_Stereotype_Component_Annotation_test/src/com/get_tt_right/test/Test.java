package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.vehicles.Car;

/*Q.	Now, how do we access these automatically created bean objects by Container that we DIDN’T configure by ourselves?
•	If we had configured the bean classes by ourselves; by using the bean name||id as an argument to getBean(“—“) method, we can access the created bean objects by Container.
Ans. As an argument to getBean() method; we need to use; getBean(Car.class), then the Container will search for Car classes, if it finds any; then that Car object it will return.
*/

public class Test {

	public static void main(String[] args) {
// Prepare Container
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		Access Container created bean objects.
		Car c = context.getBean(Car.class);
//		Invoke Car class members.
		c.printData();
	}

}
