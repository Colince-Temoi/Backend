package com.get_tt_right.functionalprogramming;

public class Main {

/* Functional Programming concepts
 * 1. Higher Order Functions
 * 2. Pure functions
 * 3. Every function must be treated as a class object
 * 
 * 1. Higher Order Function
 * -------------------------
 * - Function(s) will always allow function(s)as input(s)
 * - Function(s) will always provide a function as output.
 * 
 *  2. Pure functions
 *  -------------------------
 * - Output always depends on input. Nothing but the output/return type is same as input types. They are all of Integer type
 * - Function(s) should not have side effects. Nothing but:
 *   +. Our function(s) should not change the content of either class level static or non-static variables
 *      Nothing but they should not change the state of other objects.
 * Note: Functions never having state. Local variables we can design in our functions but we already know that , once a function is executed, its local variables are destroyed
 *       So, by default functional programming related functions not supporting states.
 *       
 * 3. Every function must be treated as a class object
 * -----------------------------------------------------
 * - Here functions have represent some memory location.
 * - If you print the hashCode to the reference in which you are storing this implementation of a functional interface, clearly you will get one hash code hence proving making this concept as valid!
 * 
 * Always Remember! : Functional programming means forwarding entire function - in this case it is an object - as argument or parameter to a method.
 * 
 * Q. Does Java support 100% functional programming?
 * Answer. No!
 * Reasons:
 *   1. Java breaches concept number 2 of functional programming as it supports partial functions as well.
 *   2. It also breaches concept number 1 as it also support Non-Higher order functions
 *   
 *   More Terminologies
 *   -------------------
 *   Partial functions - A function which can be able to change the state of our class level static and non-static variables values.
 *   Nothing but a partial function can change the state of other objects. Nothing but they are having side effects
 *   Partial functions also giving the output irrespective of the input.
 * */
	public static void main(String[] args) {
//		The entire RHSbehaves like one object. It is the implementation of I functional interface
//		Its reference we are storing into I reference
		I obj0 = (x)->{
			return x*x;
		};
		
//		Proof that the RHS of the above line is an object
		System.out.println("Hash code: "+obj0.hashCode());
//		Invoking area method of I
		Integer area = obj0.area(10);
		System.out.println(area);
		
//		Alternative1: Forwarding obj0 reference as a parameter to a method
		m1(obj0);
		
//		Alternative2: Forwarding the RHS lambda as a parameter to a method
		m1(
				(x)->{
					return x*x;
				}
		 );
		
	}
	
	public static void m1(I i) {
//		logic
		System.out.println("----------Inside m1 method---------");
		System.out.println("Hash code: "+i.hashCode());
		Integer area = i.area(10);
		System.out.println(area);
	}

}
