package com.get_tt_right.beans;

public class HelloBean {
//Static block Will be executed at the time of loading bean class byte code to the memory.
	static {
		System.out.println("Hello Bean class loading...");
	}
//Constructor will be executed eventually during the time of creating bean object.	
	public HelloBean() {
		System.out.println("Respective bean constructor will be executed and we are able to get output from this constructor");
	}
	
	public String sayHello() {
		return "HUh hah";
	}
	
//Creating bean instance By using Static Factory method. 
	/*static method is used
	 * return respective bean object.
	 * Make sure to configure this inside configuration file.>>See from there how it is done.
	 * */
	public static HelloBean getInstance() {
//		Proof that this method will be executed in order to instantiate bean class
		System.out.println("Hello Bean class Instantiation through static factory method");
		return new HelloBean();
	}
}
