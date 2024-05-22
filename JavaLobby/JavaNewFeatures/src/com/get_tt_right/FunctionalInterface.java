package com.get_tt_right;

import com.get_tt_right.interfaces.I;

/* Functional Interface
 Intro
 ------
 *- From Jdk 1.7 onwards, interfaces have been mainly classified into 3 types:
 *    + Marker/Taggling interface - does not contain any method
 *      => useful for making our object able to perform special operations like;  Serialization, Cloning, RandomAccess...etc
 *    + Non-Functional interface - contains more than one abstract method.
      + Functional Interface / Single abstract method interface
 * - Allows:
 *     + Exactly one abstract method
 *     + More than one default method
 *     + More than one static method
 * - Intention: developing functional programming in Java
 *     + Functional programming - means forwarding one function as an argument to method parameter.
 *     Useful to:
 *     => develop lambda expressions
 *     => develop method references
 *- To declare an interface as a functional interface use: @FunctionalInterface
 *- 
 *- Invoking the members of a functional interface:
 *  + Static method(s)
 *    - Directly using interface name
 *    Why should we go for static methods in interface?
 *    --------------------------------------------------
 *    . If you want to communicate with static methods, we can be able to call them directly from our main method.
 *      No need for writing implementation classes for our functional interfaces.
 *  + Default method(s)
 *    - We require an implementation class whose object we need to inject into the interface reference- Loosely coupling mechanism.
 *      . Using that interface reference invoke the default method.
 *      . You can also use this interface reference to invoke the implemented abstract method
 *    How many ways can we provide implementation for interface from Java 8?
 *    -----------------------------------------------------------------------
 *    4 ways:
 *    +. By writing a separate implementation class. - You know this.
 *    +. By writing anonymous inner class. - You also know this.
 *    +. By writing Lambdas. - Will discuss later.
 *    +. By writing Method references. - Will discuss later.
 *    
 *    Re-cap Anonymous inner class
 *    ---------------------------
 *    - Inner/Local class - Class available within another class.
 *    - Anonymous inner class - Class available inside another class but has no name.
 *    
 *    Why should we go for Anonymous inner class to provide implementations?
 *    ------------------------------------------------------------------------
 *    - Give quick! one time  implementations for an interface without writing a separate class.
 *    
 *    How to write anonymous inner class
 *    -----------------------------------
 *    +. Instantiate an interface in you class!!
 *    +. In front of the semi-colon put open and closed curly braces!!
 *     - That's it, then store this whole thing -implementation of our interface- in the interface reference variable.
 *     - This is nothing but our loosely coupling mechanism.
 *    

 * */
public class FunctionalInterface{

	public static void main(String[] args) {
//		Invoking static methods inside Functional interface
		I.SM1();
		I.SM2();
		
//		Not possible
//		I i = new I();
		
//		Anonymous Innner class way of implementation
		I i = new I() {

			@Override
			public void M1() {
				System.out.println("M1 implementation!");		
			}
			
			@Override
			public String toString() {
				return "toString rule custom implementation";
			}
		};
//		Invoking default methods inside Functional interface
//		Compiler will focus on the LHS. Is DM1 present inside I? Yes! No CE's
//		JVM will focus on the RHS. is DM1 present inside our anonymous inner class implementation? Yes! execute that
		i.DM1();
		i.DM2();
		String x =i.toString();
		System.out.println(x);
		
	}
}
