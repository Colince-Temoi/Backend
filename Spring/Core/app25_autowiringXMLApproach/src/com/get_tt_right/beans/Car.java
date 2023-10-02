package com.get_tt_right.beans;

public class Car {

//	Primitive Property(s)||Association(s)||Dependency(s)||Attribute(s)
	private String carname;

	
//	Secondary Property(s)||Association(s)||Dependency(s)||Attribute(s)
	private Engine engine;
	
//Setter method(s)--To recieve input from Container during Setter DI.
	public void setCarname(String carname) {
		this.carname = carname;
	}
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
//	Business method(s) -- Hold class functionality||logic
	public void printData() {
		System.out.println(carname);
		System.out.println(engine.getModelyear());
	}
}
