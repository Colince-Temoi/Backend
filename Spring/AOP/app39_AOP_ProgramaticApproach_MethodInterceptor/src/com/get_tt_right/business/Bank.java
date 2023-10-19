package com.get_tt_right.business;

import com.get_tt_right.exception.AccountNumberNotFoundException;

public class Bank {

//	Associations||dependencies||attributes
	private int amt=5000;
	private String accNo = "CBA123";
	
//	Setter method(s) - N/A
//	Getter method(s) - N/A
	
//	Business method(s) - Hold functionality||logic
	public int deposit(int amt, String accNo) {
		System.out.println("-------------Target class deposit method executing--------------");
		if (accNo.equals(this.accNo)) {
			System.out.println("Inside deposit method");
			this.amt+=amt;
			return this.amt;
		} else {
			throw new AccountNumberNotFoundException();
		}
	}
	
	public void anotherMethod() {
		System.out.println("---------------Target class anotherMethod executing-----------");
		System.out.println("Inside another method");
	}
	
}
