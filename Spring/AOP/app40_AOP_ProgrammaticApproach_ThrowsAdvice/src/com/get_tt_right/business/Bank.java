package com.get_tt_right.business;

import com.get_tt_right.exception.AccountNumberNotFoundException;

public class Bank {
// Associations||attributes||dependencies||variables
	int amt = 5000;
	String accNo = "CBA123";
	
//	Setter method(s) - N/A
//	Getter method(s) - N/A
	
//	Business method(s)
	public int deposit(int amt, String accNo) {
		
		if (accNo.equals(this.accNo)) {
			this.amt+=amt;
			return this.amt;
		} else {
			throw new AccountNumberNotFoundException();
		}
		 
	}
	
	public void anotherMethod() {
		System.out.println("Inside another business||target method");
	}
}
