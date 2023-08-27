package com.get_tt_right.beans;

public class HelloBean {
//Static block Will be executed at the time of loading bean class byte code to the memory.
	static {
		System.out.println("Hello Bean class loading...");
	}
//Constructor will be executed exactly at the time of creating bean object.	
	public HelloBean() {
		System.out.println("Constructor executing to now create the respective bean object.");
	}
	
	public String sayHello() {
		return "HUh hah: This business method for the bean is now accessed by using the created bean object";
	}
	
}
