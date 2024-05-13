package com.get_tt_right.staticmethodsininterface.interfaces;

public interface I {
	public default void m1() {
		System.out.println("default method from I interface");
	}

	public static void m3() {
		System.out.println("Static method in I interface");
	}
	void m2();
	
	 default void main(String[] args) {
		System.out.println("I interface main method");
	}
}
