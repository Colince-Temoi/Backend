package com.get_tt_right.beans;

/*This bean class we want to keep in thread scope.
 * For that we need to go for custom scope related implementations.
 * scopes package we need.
 * - We are going to prepare the User-defined||Custom scope here-->Thread scope that is.
 * */

public class HelloBean {

//	Static block-Executed during loading phase-Visit notes for more information.
	static {
		System.out.println("HelloBean class Loading");
	}
//Non Static Method - Executed after initialization phase.Visit notes for more.
	public HelloBean() {
		System.out.println("HelloBean class intantiation");
	}
//Non Static Method - Executed after initialization phase.Visit notes for more.
	public String sayHello() {
		return "Hello User";
	}
}
