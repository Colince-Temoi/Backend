package com.get_tt_right.beans;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HelloBean {

//dependencies||Associations||attributes
	private List<?> fruits;
	private Set<?> cricketers;
	private Map<?, ?> cc;
	
//setter methods.-->will be executed during setter-based DI.

	public void setCc(Map<?, ?> cc) {
		this.cc = cc;
	}
	public void setCricketers(Set<?> cricketers) {
		this.cricketers = cricketers;
	}
	public void setFruits(List<?> fruits) {
		this.fruits = fruits;
	}
//Business method(s)
	public void printData() {
//Iterating over List collection type
		for (Object fruit : fruits) {
			System.out.println(fruit);
		}
//Iterating over Set collection type.	
		for (Object cricketer : cricketers) {
			System.out.println(cricketer);
		}
//Iterating over Map collection type		
		Set<?> keys = cc.keySet();
		
		for (Object key : keys) {
			System.out.println("Country = "+key+" Capital = "+cc.get(key));
		}
	}
	
}
