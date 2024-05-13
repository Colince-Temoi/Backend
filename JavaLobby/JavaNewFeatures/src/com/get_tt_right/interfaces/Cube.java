package com.get_tt_right.interfaces;

public interface Cube {
	public default void cal(int s) {
		System.out.println("The Cube of " +s+" is: "+s*s*s);
	}
}
