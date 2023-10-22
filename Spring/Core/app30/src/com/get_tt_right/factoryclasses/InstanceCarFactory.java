package com.get_tt_right.factoryclasses;

import com.get_tt_right.interfaces.Car;

/*Instance factory class --Has a Non-static||instance factory-method.
 * Maintain a Non-static association||variable||attribute||dependency -- Will hold our input.
 * The input here is the fully qualified class name of implementation partner class.
 * Maintain a setter method to the attribute discussed above.
 * The setter method will receive input from Container during setter method DI.
 * Make its constructor private -- To prevent direct instantiation of this class from outside. But spring can instantiate it anyways. haha
 * The factory-method --Holds instantiation logic to implementation partners classes.
 * 
 * */
public class InstanceCarFactory {
//	primitive variable||association||dependency
	private String carname;

//	private constructor
	public InstanceCarFactory(){

	}
	
// Setter method	
	public void setCarname(String carname) {
		this.carname = carname;
	}

//Instance-factory method
	public Car getCar() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
//		Returned object we are upcasting -Storing into interface reference
		Car car = (Car) Class.forName(carname).newInstance();	
		return car;
	}
}
/*NOTES:
 * Writing a factory class is the second thing we need to provide when preparing factory classes.
 * Above is how to prepare an Instance factory class.
 * Inside the factory method:
 *   - We are not writing any singleton logic as you can see.
 *   - By default Spring will make this returned object Singleton.
 *Now; this prepared instance factory class is capable to create implementation partners' class objects.
 *In future; if you have one more implementation class from some other implementation partner happily that implementation partner class object this factory class can be able to create.
 *The best suitable example of an instance factory class is; Hibernate SessionFactory class.
 *In the same way, we have designed this InstanceCarFactory class. We have designed it using the approach:
 *   - Instance factory class approach.
 * */
