package com.get_tt_right.beans;

public class Car {

//Association(s)||dependency(s)|| attribute(s)|| property(s)
	private String carname;
	private Engine engine;
	
//Setter method(s)-- To receive input from IOC container during Setter-based DI
	public void setCarname(String carname) {
		this.carname = carname;
	}
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
//Business method(s)--To write||implement Business logic
	public void printData() {
		System.out.println(carname);
		System.out.println(engine.getModelyear());
	}
}
