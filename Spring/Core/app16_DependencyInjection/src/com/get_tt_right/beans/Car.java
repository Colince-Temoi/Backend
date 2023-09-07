package com.get_tt_right.beans;

/*We need the primitive and secondary data types association inside this class
 * The IOC container will pass||inject them for us once it reads the configurations from the xml file.
 * */

public class Car {

//	primitive data type association.
	private String carname;
//	Secondary data type association.
	private Engine engine;
	
	public void setCarname(String carname) {
		this.carname = carname;
	}
	
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
//	Business method.
	public void printCarData() {
		System.out.println("---------Car data--------");
		System.out.println("Car name = "+carname);
		System.out.println("Model year = "+ engine.getModelyear());
	}
}
