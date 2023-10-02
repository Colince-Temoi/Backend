package com.get_tt_right.beans;

public class Engine {
//	Primitive Property(s)||Association(s)||Dependency(s)||Attribute(s)
	private String modelyear;

	
//	Secondary Property(s)||Association(s)||Dependency(s)||Attribute(s) -- NA
	
	
//Setter method(s)--To receive input from Container during Setter DI.
	public void setModelyear(String modelyear) {
		this.modelyear = modelyear;
	}
	
//	getter method(s) -- To access respective Property(s)||Association(s)||Dependency(s)||Attribute(s) from outside this bean class.
	public String getModelyear() {
		return modelyear;
	}
	
//	Business method(s) -- Hold class functionality||logic --NA

}
