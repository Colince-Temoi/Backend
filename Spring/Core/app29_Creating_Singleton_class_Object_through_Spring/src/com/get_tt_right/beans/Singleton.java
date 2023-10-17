package com.get_tt_right.beans;
/*Writing this class as a singleton class
 * We have to provide a static factory method - Holds the instantiation logic to this class.
     - Its return type is; this same class.
 * We need to also maintain this same class; class level static reference variable||association.
    - It will store the the only created reference on this class.
 * NOTE: In case of Singleton; we  need to restrict; Cloneable access also. How?
    -Override clone method
    -Inside its body; throw new CloneNotSupportException()
 * Means nothing but; whenever you try to clone it; it will not support for cloning.
 * 
 * Directly using new keyword, we cannot instantiate Singleton class object from outside this class;
   as well as we cannot perform cloning also in cases after we have created this class object by using static method.
 * The only way to get this class object is through; getInstance() method static factory-method.
 * */
public class Singleton {

//	Primitive attribute(s)||association(s)||dependency(s)||variables
	private static Singleton singleton;
	
//	Private Constructor -- To prevent creation of this class objects from outside. But Spring can create anyways. haha.
	private Singleton(){
//		To prevent creation objects of this class by even Spring.
			
	}
	
//	Setter method(s) -- To receive input from Container during Setter DI. -- N/A
	
//	factory-method -- Holds instantiation logic to Calendar class.
	public static Singleton getInstance() {
		if (singleton==null) {
			singleton=new Singleton();
			return singleton;
		}
		return singleton;
	}
//	Restrict cloneable access
	@Override
		protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
		}
}
/*NOTES
 * private constructor classes objects can also be created by Spring.
 * 
 * */
