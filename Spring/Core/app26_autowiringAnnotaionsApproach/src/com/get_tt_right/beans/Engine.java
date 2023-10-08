package com.get_tt_right.beans;

public class Engine {

//	primitive association(s)||dependency(s)||attribute(s)||property(s)
	private String modelyear;
//	Secondary association(s)||dependency(s)||attribute(s)||property(s)-NA

//	setter method(s) - To receive input from Container  during Setter DI
	public void setModelyear(String modelyear) {
		this.modelyear = modelyear;
	}

//	Getter method(s) - To access respective association(s)||dependency(s)||attribute(s)||property(s) from outside this class in other classes that have an association to this class.
	public String getModelyear() {
		return modelyear;
	}



}
