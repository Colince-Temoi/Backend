package com.get_tt_right.interfaces;

@FunctionalInterface
public interface I {
//	 Allows only one abstract method
	public abstract void M1();
//	public abstract void M2();
	
//	Allows more than 1 default methods
	public default void DM1() {
		System.out.println("Default method 1");
	}	
	public default void DM2() {
		System.out.println("Default method 2");
	}
	
//	Allows more than 1 Static methods
	public static void SM1() {
		System.out.println("Static method 1");
	}
	public static void SM2() {
		System.out.println("Static method 2");
	}
}
