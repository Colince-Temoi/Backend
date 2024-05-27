package com.get_tt_right.functionalprogramming;

public class PureAndPartialFunctions2 {
	
	void check() {
		
		System.out.println("------------Inside check method-----------");

//		The RHS is a pure function. Reason: It's output/return type is same as input types. They are all of Integer type
		J2 obj1 = (x,y)->{
		    System.out.println("The Type of "+ x + " : " + x.getClass().getName());
		    System.out.println("The Type of "+ y + " : " + y.getClass().getName());
			return x*y;
		};
		
		Integer output = obj1.m1(10, 20);
	    System.out.println("The Type of "+ output + " : " + output.getClass().getName());

	}


	public static void main(String[] args) {
//		The RHS is not a pure function. Reason: It's output/return type is same as input types. The inputs are of Integer type whereas the output/return type is of string type.
		J3 obj0 = (x,y)->{
			System.out.println("The Type of "+ x + " : " + x.getClass().getName());
		    System.out.println("The Type of "+ y + " : " + y.getClass().getName());
			return "The result is "+x*y;
		};
//		Invoking m1 method of J3
		String output = obj0.m1(30, 40);
	    System.out.println("The Type of "+ output + " : " + output.getClass().getName());
		
//		Alternative1: Forwarding obj0 reference as a parameter to a method
		m1(obj0);
		
//		Alternative2: Forwarding the RHS lambda as a parameter to a method
		m1(
				(x,y)->{
					System.out.println("The Type of "+ x + " : " + x.getClass().getName());
				    System.out.println("The Type of "+ y + " : " + y.getClass().getName());
					return "The result is "+x*y;
				}
				
		 );
		
//		by using anonymous object I am calling check method of this class
		new PureAndPartialFunctions2().check();
		
	}
	
	public static void m1(J3 j) {
//		logic
		System.out.println("----------Inside m1 method---------");
		System.out.println("Hash code: "+j.hashCode());
		String output = j.m1(50,60);
	    System.out.println("The Type of "+ output + " : " + output.getClass().getName());
	}

}
