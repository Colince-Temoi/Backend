package com.get_tt_right.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.get_tt_right.interfaces.Car;

public class Test {

	public static void main(String[] args) {
//		Prepare Container
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		Access Container Created objects
		Car car = (Car) context.getBean("car");
		Car car1 = (Car) context.getBean("car");
		
		Car car2 = (Car) context.getBean("carfrominstancefactoryclass");
		
		Car car3 = (Car) context.getBean("factorybeaninterfacecarfactory");
		
//		Singleton test	
		System.out.println(car==car1); // true: id is same and Spring will make  configured beans with same id by default as singleton.
		System.out.println(car==car2); // car,car2 and car3 are 3 separate and different objects.
		
//		Access the respective class objects members(data)
		car.drive();
		car2.drive();
		car3.drive();
	}

}
/*NOTES:
 * From the id; car||carfrominstancefactoryclass||factorybeaninterfacecarfactory, we are expecting to get the required implementation partner class object.
 * That expected object we are getting it in the form of Car(I) reference.
 * Now, using the Car(I) reference, we can happily be able to access implementation partner class members(data)
 * In case if I don't wan't a particular implementation partner class instance; I can input my preferred and I will get my required instance||object and I will use it.
 * Like this Client is:
 *   - Independent of the implementation partner. We are not bound to any.
 * */
