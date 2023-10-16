package com.get_tt_right.business;

import com.get_tt_right.exception.AccountNumberNotFoundException;

public class Bank {

//	attributes||dependencies||associations
	private int amt = 5000;
	private String accNo = "CBA123";
	
//	setters - N/A
//	getters - N/A
	
//	Business method(s)
	public int deposit(int amt, String accNo) {
		
        System.out.println();
        System.out.println("------------------deposit method executing-------------------------------");
        
		if (accNo.equals(this.accNo)) {
			System.out.println("In deposit method");
			this.amt+=amt;
			return this.amt;
		} else {
			throw new AccountNumberNotFoundException();
		}
	}
	
	public void anotherMethod() {
		System.out.println();
        System.out.println("-----------------anotherMethod executing--------------------------------");
		System.out.println("I am executing first then service will execute");
	}
}
