package com.get_tt_right.beans;

public class Engine {

//Association(s)||dependencie(s)||attribute(s)||propertie(s)
	private String modelyear;
	
//Setter method(s) -- To recieve input from IOC container during Setter-based DI
	public void setModelyear(String modelyear) {
		this.modelyear = modelyear;
	}
// Getter method(s) -- May be used to access this class Association(s)||dependencie(s)||attribute(s)||propertie(s) from other bean class(s)
	public String getModelyear() {
		return modelyear;
	}
//Business method(s) -- To write||business businesslogic.
}
