package com.get_tt_right.beans;

public class Car {

//	Primitive static attribute(s)||dependency(s)||association(s)
	private static String carname;

//	Setter method - To receive input from Container during static Setter DI.
	public static void setCarname(String carname) {
		Car.carname = carname;
	}
	
//	Business method(s)
	public static void printCarName() {
		System.out.println("Car name is: "+carname);
	}
	
	
}
