package com.get_tt_right.business;

import com.get_tt_right.exception.AccountNumberNotFoundException;

public class Bank {

//	Associations||dependencies||attributes
	private int amt;
	private String accNo;
	
//	Setter method(s) --> To accept input from container during Setter DI
	public void setAmt(int amt) {
		this.amt = amt;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	
//	Business method(s) --> Hold particular business logic or functionality.
	
//	Deposit method
	public int deposit(int amt, String accNo) {
		if (accNo.equals(this.accNo)) {
			System.out.println("Inside deposit method");
//			deposit operation
			this.amt+=amt;
			System.out.println(this.amt);
			return this.amt;
		} else {
//			Exception code goes here
			throw new AccountNumberNotFoundException();
		}
	}

//	Find balance method
	public int findBal(String accNo) {
		if (accNo.equals(this.accNo)) {
//			find balance operation
			return this.amt;
		} else {
			throw new AccountNumberNotFoundException();
		}
	}
}
