package com.get_tt_right.beans;

/**
 * @author tmi
 *
 */
public class HelloBean {

	private String name;
	private String message;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String sayHello() {
		return "Hello "+name+" "+message;
	}

	/*
	 * Initializing Bean object through customInit() method.
	 * Make sure to tell the Container about this inside the configuration file-applicationContext.xml file
	 * That is; utilize init-method attribute inside the "bean" tag during Bean configuration
	 */
	public void customInit() {
		System.out.println("customInit()-HelloBean");
		name="Colince";
		message="Good Morning";
	}
	public void customDestroy() {
		System.out.println("customDestroy()-HelloBean");
	}
	
	
}
