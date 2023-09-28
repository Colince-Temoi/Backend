package com.get_tt_right.beans;

public class Car {

//	Primitive Association(s)||attribute(s)||dependencie(s)
	private String carname;
	
//	Secondary Association(s)||attribute(s)||dependencie(s)
	private Engine engine;
	
//	Setter method(s)- To recieve input from Container during Setter DI.
	public void setCarname(String carname) {
		this.carname = carname;
	}
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
//	Business method(s) - Contain the logic||Functionality of this class.
	public void printData() {
		System.out.println(carname);
		System.out.println(engine.getModelyear());
	}
	
}
