package com.get_tt_right.defaultmethodtestclasses;

import com.get_tt_right.interfaces.I;
import com.get_tt_right.interfaces.J;

public class DefaultMethodTestClassA implements I, J{

	@Override
	public void M1() {
		System.out.println("M1 method implementation in A");
	}
//	Default methods are not able to participate in overriding
//	To make them participate, omit the keyword: default
//	@Override
//	public default void DM2(int x) {
//		System.out.println("Default method 2: "+x);
//	}
	@Override
	public void DM2(int x) {
		System.out.println("Overriden default method 2: "+x);
	}
}
