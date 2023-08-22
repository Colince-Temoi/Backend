package com.get_tt_right.beans;
// Bean Class
public class WelcomeBean {
//Property
	private String name;
//Static Block
	static {
		System.out.println("WelcomeBean Class Loading.....");
	}
//Default constructor I am taking.	
	public WelcomeBean() {
		System.out.println("Welcome Bean class Instantiation...");
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWelcomeMessage() {
		return "Hello "+name+"!, welcome to get it right software solutions";
	}
}
