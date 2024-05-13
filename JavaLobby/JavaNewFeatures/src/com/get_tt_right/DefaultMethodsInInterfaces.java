package com.get_tt_right;

import com.get_tt_right.defaultmethodtestclasses.DefaultMethodTestClassA;
import com.get_tt_right.defaultmethodtestclasses.DefaultMethodTestClassB;
import com.get_tt_right.defaultmethodtestclasses.DefaultMethodTestClassC;
import com.get_tt_right.interfaces.I;
import com.get_tt_right.interfaces.Square;

public class DefaultMethodsInInterfaces {

	/* Default Methods in Interface - Introduced in Jdk 1.8
	 * Reason: W/out disturbing implementation classes of interface, if you want to add a new service, then write default methods in the interface.
	 * Before this concept we only had Abstract methods in interfaces and thus any class implementing the interface must respect the forcibility rule.
	 * Solutions to prevent disturbing implementation classes as they unfolded over-time:
	 * 1. interface xyz extends zyx
	 *  - Here w/out disturbing the implementation class(s) of zyx we can add more specifications to xyz.
	 *  - Problem will recur for classes that implement xyz: Forcibility and Extensibility will be compulsory.
	 * 2. Adapter Pattern
	 *  - Abstract class which is going to provide empty implementations.
	 *  - On extending this class in our implementation classes; forcibility is not an issue now.
	 *  - Problem: Any class that extends this AC class will not be able to extend any other class. Java does not support Multiple inheritance.
	 * 3. Default methods
	 *  - syntax: AccessModifier default return-type methodname(Parameter(s)){-----}
	 *  - Note: If you are adding any logic to interfaces by using default methods, that will be automatically coming to all implementation classes of that interface-Inheritance
	 *          private access modifier is not allowed. Only; default, abstract, static and strictfp are permitted.
	 *  - Default methods can be multiple in an interface and can also be overloaded and overridden. 
	 *  - They cannot be able to participate in overriding: Reason:  Default methods cannot be written in classes and abstract classes.        
	 *    +. It is only possible provided we should drop keyword like default in the overridden default methods. Reason: Methods with keyword like default/ nothing but default methods are only allowed in interface and not in abstract classes and classes.
	 *  - If I have 2 different interfaces having similar default methods and then implementing these two interfaces in my class. Ambiguity issues we will face. To overcome this; override the default method in your class.
	 *    +. When you call this default method it will now be executing from your class. 
	 *  - From jdk 1.8 java people providing the flexibility to write default methods with logic in interface but the default method's name should not be equal to any of the 11 method names of java.lang.Object class. Else we are going to get errors.
	 *  
	 * 
	 * */
	public static void main(String[] args)
	{
		I obj = new DefaultMethodTestClassA(); // loosely coupling
		obj.M1();
		obj.DM1();
		obj.DM2(20);
		obj = new DefaultMethodTestClassB();
		obj.M1();
		obj.DM1();
		obj.DM2(10);
		
		Square obj1 = new DefaultMethodTestClassC(); // loosely coupling
		obj1.cal(10);

	}

}
