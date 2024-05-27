package com.get_tt_right.functionalprogramming;

public class HigherOrderFunction {
//	Im am increasing the scope of obj0, obj1 and obj2
	static K obj0;
	static L obj1;
	static N obj2;
	
	void check() {
//		The RHS is a function object for K functional interface
		obj0 = ()->{
			return 111;
		}; 
		
//		The RHS is a function object for L functional interface
		obj1 = ()->{
			return 222;
		};
//		The RHS is a function object - A Higher Order Function actually- for N functional interface
//		It takes obj0 and obj1 as inputs and returns obj3 as the output
//		These inputs and outputs are function objects actually. Hahaah
		 N2 obj2 = (K k, L l)->{
			System.out.println("M4 executing"+k.m1()+l.m2());
//			return ()->{}; // Like this you can return a function object
			
//			Like this, you could also return
			M obj3 = ()->{
				System.out.println("m3()");
			};
			
			return obj3;
		};
		
//		Invoking m4 method by passing two function reference variables
//		m4 method has passes all of the thumb rules for a Higher Order Function
		obj2.m4(obj0, obj1);

	}


	public static void main(String[] args) {
//		The RHS is a function object for K functional interface
		obj0 = ()->{
			return 111;
		}; 
		
//		The RHS is a function object for L functional interface
		obj1 = ()->{
			return 222;
		};
//		The RHS is a function object for N functional interface
//		It takes obj0 and obj1 as inputs
		obj2 = (K k, L l)->{
			System.out.println("M4 executing"+k.m1()+l.m2());
		};
		
//		Invoking m4 method by passing two function reference variables
//		m4 method has passes on of the thumb rules for a Higher Order Function
//		It is still not a Higher Order function because it's output/return type is not a function object.
		obj2.m4(obj0, obj1);
			
//		Alternative1: Forwarding obj2 reference as a parameter to a method
		m1(obj2);
		
//		Alternative2: Forwarding the RHS lambda as a parameter to a method
		m1(
				(K k, L l)->{
					System.out.println("m4 executing "+k.m1()+l.m2());
				}
				
		 );
		
//		by using anonymous object I am calling check method of this class
//		new HigherOrderFunction().check();
		
	}
	
	public static void m1(N obj2) {
//		logic
		System.out.println("----------Inside m1 method---------");
//		System.out.println("Hash code: "+obj2.hashCode());
		obj2.m4(obj0, obj1);
	}

}
