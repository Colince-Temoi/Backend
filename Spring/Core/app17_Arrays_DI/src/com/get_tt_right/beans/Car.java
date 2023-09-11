package com.get_tt_right.beans;

/*In this class, we are maintaining 2 associations||dependencies||attributes;
1. Primitive array type for the carnames
2. Secondary array type for the engines

* Setter methods--To recieve input from the IOC container during Setter based DI.
* Getter methods -- Can be used to get this class' data from other classes that are having a dependency of this class.
*/
public class Car {
	
//Dependencies||Associations||attributes
	private String[] carnames;
	private Engine[] engines;
	
//Setter and getter methods.
	public String[] getCarnames() {
		return carnames;
	}
	public void setCarnames(String[] carnames) {
		this.carnames = carnames;
	}
	public Engine[] getEngines() {
		return engines;
	}
	public void setEngines(Engine[] engines) {
		this.engines = engines;
	}
// Business method-To print the carnames and engines data from the respective arrays.	
	public void printCarData() {
		for (String car : carnames) {
			System.out.println(car);
		}
		for (Engine engine : engines) {
			System.out.println(engine.getModelyear());
		}
	}
	
	
}
