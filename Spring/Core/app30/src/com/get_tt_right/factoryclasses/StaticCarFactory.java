package com.get_tt_right.factoryclasses;

import com.get_tt_right.interfaces.Car;

/*static factory class --Has static factory-method.
 * Maintain a static association||variable||attribute||dependency -- Will hold our input.
 * The input here is the fully qualified class name of implementation partner class.
 * Maintain a static setter method to the attribute discussed above.
 * The static setter method will receive input from Container during static setter method DI.
 * Make its constructor private -- To prevent direct instantiation of this class from outside. But spring can instantiate it anyways. haha
 * The static factory-method --Holds instantiation logic to implementation partners classes.
 * 
 * */
public class StaticCarFactory {
//	primitive static variable||association||dependency
	private static String carname;

//	private constructor
	private StaticCarFactory() throws IllegalAccessException {
//		To prevent creation of this class object through Containers like Spring.
		throw new IllegalAccessException();
	}
	
//static Setter method	
	public static void setCarname(String carname) {
		StaticCarFactory.carname = carname;
	}

//static-factory method
	public static Car getCar() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
//		Returned object we are upcasting -Storing into interface reference
		Car car = (Car) Class.forName(carname).newInstance();	
		return car;
	}
}
/*NOTES:
 * Writing a factory class is the second thing we need to provide when preparing factory classes.
 * Above is how to prepare a static factory class.
 * Inside the static-factory method:
 *   - We are not writing any singleton logic as you can see.
 *   - By default Spring will make this returned object Singleton.
 *Now; this prepared static factory class is capable to create implementation partners' class objects.
 *In future; if you have one more implementation class from some other implementation partner happily that implementation partner class object this factory class can be able to create.
 *The best suitable example of a static factory class is; DriverManager class.
 *   - It can create instance||object of any implementation class(DB vendor implementation classes)
 *In the same way, we have designed this StaticCarFactory class. We have designed it using the approach:
 *   - static factory class approach.
 * */
