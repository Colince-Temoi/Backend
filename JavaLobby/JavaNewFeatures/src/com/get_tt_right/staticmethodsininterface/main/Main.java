package com.get_tt_right.staticmethodsininterface.main;

import com.get_tt_right.staticmethodsininterface.interfaces.A;
import com.get_tt_right.staticmethodsininterface.interfaces.I;

/* Static methods in interface
 * Main intention?
 * From Jdk 1.8 you can write logic in interface through default methods.
 * To execute the interface default methods we require implementation classes.Either:
 *   1. Create a separate class which implements our interface.
 *   2. Anonymous inner class as implementation class for interface
 * We can't execute default method by calling them using interface name.
 * We can only do so using the implementation class reference or call it inside a method of the implementation class
 * What if I don't want to do this? I don't want implementation classes! but I want to execute logic of interface(s)?
 * Sln: Go for using static methods in interface - introduced in Jdk 1.8
 * You cannot override static methods of interface.
 * Rule: static methods not participating in method overriding. Nothing but common/sharable data cannot be changed by an individual/class/person
 * Note:
 * ------
 * 1. upto jdk 1.4: by using a class or AC we can create a program, compile and execute it.
 * 2. from jdk 1.5 onwards: Enum concept introduced.
 *    - With the support of Enum, we can write: static methods, main method, static main method
 *    - We can compile and execute a program from an Enum.
 * 3. From jdk 1.8 onwards, we can also happily compile and execute our programs with the support of interface alone.
 * 
 * 
 * */
public class Main {

	public static void main(String[] args) {
		A a = new A();
//		Invoking default methods of interface I
		a.m1();
		
//		OR
		a.m2();
		
//		Through anonymous inner class
		I i = new I() {
			@Override
			public void m2() {
				System.out.println("Inside anonymous inner class behavior");
				m1();
			}
		};
		i.m1();
		i.m2();
		
//		Alternative B : Executing logic of interface without the need of an implementation class
		I.m3();	

	}

}
