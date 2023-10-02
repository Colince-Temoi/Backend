package com.get_tt_right.beans;

public class Bus {

//	Primitive Property(s)||Association(s)||Dependency(s)||Attribute(s)
	private String busname;

	
//	Secondary Property(s)||Association(s)||Dependency(s)||Attribute(s)
	private Engine engine;
	
//Setter method(s)--To recieve input from Container during Setter DI.
	public void setCarname(String carname) {
		this.busname = carname;
	}
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
//	Param constructor-To receive input from Spring Container during Secondary Param Constructor DI.
	public Bus(String busname, Engine engine) {
		super();
		this.busname = busname;
		this.engine = engine;
	}
	
//	Business method(s) -- Hold class functionality||logic
	public void printData() {
		System.out.println(busname);
		System.out.println(engine.getModelyear());
	}

	
}
