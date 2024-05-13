package com.get_tt_right.interfaces;

public interface Square {
	public default void cal(int s) {
		System.out.println("The square of " +s+" is: "+s*s);
	}
}
