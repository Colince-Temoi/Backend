package com.get_tt_right;

import com.get_tt_right.interfaces.I;
import com.get_tt_right.interfaces.LambdaInterface;
/*
 * () will be replaced internally by the method name i.e., m1 of I Functional Interface
 *  -> Lambda related operator
 *  
 * - With the help of lambdas we can be able to develop functional programming in Java from jdk 1.8 onwards
 * - Mainly lambdas are designed for providing implementation for single abstract method /functional interfaces within a very short time and concise code.
 * - Lambdas are very useful when work with Collection framework objects for iterating, filtering, mapping, counting, ...etc. Different operations we can perform on top of Collection Framework related objects with the support of lambda expressions. You will understand better when we will be discussing Stream API concepts
 * - Lambda expressions are only suitable for FunctionalInterface but NOT! for Non-Functional/Normal Interfaces
 * - We have variety of flexibility with which we can write syntax for lambda, this depends on whether our functional interface's abstract method has arguments or not.
 * - Syntax: (datatype parameter, datatype parameter, ...) -> {statements};
 * 
 * - Lambdas/anonymous functions/ anonymous method/ unnamed methods
 * - They don'nt maintain .class files like classes(both named and unnamed classes)
 * - A normal java method is a member of a class whereas a lambda is a class level object.
 * - 
 * */
public class LambdaImplementaionStyle {

	public static void main(String[] args) {
//		Everything behind the > symbol represents the implementation for I Functional interface m1 method
		I obj = ()->System.out.println("M1 lambda");
		obj.M1();
		
		LambdaInterface obj1 = (x)->System.out.println("Value of x is "+x);
		obj1.m2(10);

	}

}
