package com.get_tt_right.interfaces;

/*
 * Can allow more than one abstract method but on condition:
 * - 2nd abstract method onwards must and should be part of java.lang.Object class methods
 * - You may provide implementations for such type of abstract methods if you wish else no problem as you will not face any errors since there implementations are provided in the background by java.lang.Object super class.
 * - Nothing but when you will be implementing this interface - we know that every implementation class (Known or anonymous) has java.lang.Object class as its parent.
 * - Therefore when JVM will be searching for the implementation for such type of abstract methods, if it will not find them in our implementation class it will contact the super class and without any doubt will use those implementations.
 * - If we choose to provide implementation for such type of abstract methods inside our implementation class, means that we have overridden java.lang.Object provided implementation and this is what the JVM will execute.
 * 
 * - Functional interfaces are eligible for participating in inheritance but on condition that they must and should be maintaining only one abstract method which is inherited from the super/parent functional interface.
 * - 
 * 
 * */
@FunctionalInterface
public interface I {
//	 Allows only one abstract method
	public abstract void M1();
//	 Can allow more than one abstract method on condition the 2nd abstract method onwards is a java.lang.Object method
	public abstract String toString();
	public abstract int hashCode();
//	public abstract void M2();
	
//	Allows more than 1 default methods
	public default void DM1() {
		System.out.println("Default method 1");
	}	
	public default void DM2() {
		System.out.println("Default method 2");
	}
//	Default methods can be overloaded
	public default void DM2(int x) {
		System.out.println("Default method 2: "+x);
	}
	
//	Allows more than 1 Static methods
	public static void SM1() {
		System.out.println("Static method 1");
	}
	public static void SM2() {
		System.out.println("Static method 2");
	}
}
