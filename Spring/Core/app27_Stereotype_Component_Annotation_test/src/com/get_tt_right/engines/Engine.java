package com.get_tt_right.engines;

public class Engine {
// Primitive Association(s)||attribute(s)||Dependency(s)
	private String modelyear;
	
//	Setter method(s) -- To receive input from Container during Setter DI
	public String getModelyear() {
		return modelyear;
	}
// Getter method(s) -- To access the Association(s)||attribute(s)||Dependency(s) from other classes having this Class as a dependency.
	public void setModelyear(String modelyear) {
		this.modelyear = modelyear;
	}
//Business method(s) -- To hold functionality||logic --NA
	
}
