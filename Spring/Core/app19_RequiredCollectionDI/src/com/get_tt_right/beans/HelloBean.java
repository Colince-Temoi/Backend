package com.get_tt_right.beans;

import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class HelloBean {

//attributes||dependencies||associations
	private Vector<?> fruits;
	private TreeSet<?> criketers;
	private Hashtable<?, ?> cc;
	
//Setter methods -- To recieve input from IOC container during Setter based DI.
	public void setFruits(Vector<?> fruits) {
		this.fruits = fruits;
	}
	public void setCriketers(TreeSet<?> criketers) {
		this.criketers = criketers;
	}
	public void setCc(Hashtable<?, ?> cc) {
		this.cc = cc;
	}
	
//Business methods -- Utilize the injected data.
	public void printData() {
//Iterating, accessing and printing Vector(C) data using for..each method.
		System.out.println("------Fruits------------");
		for (Object fruit : fruits) {
			System.out.println(fruit);
		}
		
//Iterating, accessing and printing TressSet(C) data using for..each method.
		System.out.println("------Criketers------------");
		for (Object criketer : criketers) {
			System.out.println(criketer);
		}
		
/*Iterating, accessing and printing HashTable(C) data using for..each method. Steps:
 * 1. Access the keys of HashTable collection by invoking keySet() method on top of Hashtable reference and store those keys in a Set collection.
 * 2. Itarate over the Set keys using for..each method.
 * 3. Now, by invoking get method on top of HashTable reference and passing the respective key from the Set of keys
 *    during each iteration, you can happily access and utilize the values to the respective keys as you wish.
 * For, our case we are just going to print the respective keys and values from the HashTable.
 *    
 */
		System.out.println("------Countries and capitals------------");
		Set<?> keys = cc.keySet();
		
		for (Object key : keys) {
			System.out.println("Country: "+key+" Capital: "+cc.get(key));
		}
	}
}
