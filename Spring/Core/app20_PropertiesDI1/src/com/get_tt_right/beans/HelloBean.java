package com.get_tt_right.beans;

import java.util.Properties;
import java.util.Set;

/*Logic to iterate over Map(I) implemetation classes[Properties(C) in particular] by utilizing for..each method
1. On top of Properties association||attribute||dependency reference (i.e., driver) invoke keySet() method.
2. Store the result|| key set in Set(I) collection.
3. iterate over the key set using for..each method.
4. To access the respective value to a particular key; inside the body of the for..each method, on top of
   Properties association||attribute||dependency reference (i.e., driver) invoke getProperty() method with key as its argument
5. And like this you have access to complete data[keys and values] to work with as per your requirement from Properties association||attribute||dependency of this bean class.
*/
public class HelloBean {
//dependencies||Associations||attributes
	private Properties driver;
	
//Setter method(s) -- To recieve input from IOC container during setter-based DI.
	public void setDriver(Properties driver) {
		this.driver = driver;
	}
	
//Business method(s)
	public void printData() {
		
//Accessing the keys and Storung them into Set(I) collection.
		Set<?> keys = driver.keySet();
//Iterating over the keys and accessing their respective values.		
		for (Object key : keys) {
			System.out.println("Property: "+key+" | Value: "+driver.getProperty((String) key));
		}
		
	}
}
