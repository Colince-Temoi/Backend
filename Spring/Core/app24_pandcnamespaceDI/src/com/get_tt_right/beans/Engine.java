package com.get_tt_right.beans;

public class Engine {

//	Primitive Properties||Associations||Dependencies
	private String modelyear;
	
//	getter method(s) - Will be used to access this class' data from outside in other classes
	public String getModelyear() {
		return modelyear;
	}

//	Parameterized constructor-Will receive input from Container during Constructor DI
public Engine(String modelyear) {
	super();
	this.modelyear = modelyear;
	
//	Business method(s) - N/A
}
	
	
	
}
