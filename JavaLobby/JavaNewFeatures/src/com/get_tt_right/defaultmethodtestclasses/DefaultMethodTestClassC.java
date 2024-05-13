package com.get_tt_right.defaultmethodtestclasses;

import com.get_tt_right.interfaces.Cube;
import com.get_tt_right.interfaces.I;
import com.get_tt_right.interfaces.J;
import com.get_tt_right.interfaces.Square;
/* If both interfaces having the same default methods and your class implements both the interfaces by using concept like: Multiple inheritance, definitely we are going to face some ambiguity problem by the compiler!
 * To overcome that problem we should override cal(-) method in our class.
 * If you are creating an object for this class and using it's reference to call/invoke cal(-) method, then cal(-) method will be executing from this class.
 * If you want to execute cal(-) method logic of our interfaces i.e., Square or Cube then by using a syntax like:
 *    +. Square.super.cal(20) - We can be able to execute the cal(-) method from interface like Square.
 *    +. Cube.super.cal(10) - We can be able to execute logic of cal(-) method from the interface like Cube.
 * */
public class DefaultMethodTestClassC implements Square, Cube{

	@Override
	public void cal(int s) {
		System.out.println("Overriden cal(-) method executing!");
		Square.super.cal(20);
		Cube.super.cal(10);
	}
}
