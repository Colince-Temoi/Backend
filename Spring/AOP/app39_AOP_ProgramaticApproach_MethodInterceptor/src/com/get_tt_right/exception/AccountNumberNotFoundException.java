package com.get_tt_right.exception;
/*
 * Am extending this AccountNumberNotFoundException class from RuntimeException predefine class; Reason: 
    - When we throw this AccountNumberNotFoundException anywhere in our project, we wont be asked to handle any other exceptions that may arise as a result of throwing this AccountNumberNotFoundException
 */
public class AccountNumberNotFoundException extends RuntimeException {

	@Override
	public String toString() {
		return "Invalid Account Number!";
	}
}
