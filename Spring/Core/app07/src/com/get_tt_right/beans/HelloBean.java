package com.get_tt_right.beans;

public class HelloBean {

	private String name;
	
	static {
		System.out.println("HelloBean Class loading....");
	}
	
	public HelloBean() {
		System.out.println("HelloBean Class Instantiation...");
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String sayHello() {
		return "Hello "+name+"!, Good Morning";
	}
}
