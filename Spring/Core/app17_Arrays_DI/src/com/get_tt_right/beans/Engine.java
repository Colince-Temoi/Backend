package com.get_tt_right.beans;

/*Maintaining only one String primitive array dependency to store modelyears
setter--To recieve input from IOC container inputed from xml file.
getter--Can be used to pass data into another class that has this class as an association||dependency

*
*/
public class Engine {

// Dependency||Association||Attribute
	private String modelyear;

// Setter and getter methods
	public String getModelyear() {
		return modelyear;
	}

	public void setModelyear(String modelyear) {
		this.modelyear = modelyear;
	}
// No business methods.
	
}
