package com.get_tt_right.vehicles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.get_tt_right.engines.Engine;
@Component
public class Car {
// Secondary Association(s)||attribute(s)||Dependency(s)
	@Autowired
	@Qualifier(value = "e2")
	private Engine engine;
	
// No need to maintain Setter||param constructors here to recieve input from Container during DI as the property level annoations have direct access to the varibales and finally can do the assigning of required values||dependecies.

//Business method(s) -- To hold functionality||logic
	public void printData() {
		System.out.println("Engine model year: "+engine.getModelyear());
	}

}
