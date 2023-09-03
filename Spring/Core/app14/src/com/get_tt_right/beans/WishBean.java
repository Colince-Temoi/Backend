package com.get_tt_right.beans;

public class WishBean {

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
	
	/*
	 * Initializing Bean object through customInit() method.
	 * Make sure to tell the Container about this inside the configuration file-applicationContext.xml file
	 * That is; utilize init-method attribute inside the "bean" tag during Bean configuration
	 */
	public void customInit() {
		System.out.println("customInit()-WishBean");
		name="Colince";
		message="Good Morning";
	}
	
	public void customDestroy() {
		System.out.println("customDestroy()-WishBean");
	}
	
//	Business method.
	public String  sayWish() {
		return "Hello "+name+" "+message;
	}
}
