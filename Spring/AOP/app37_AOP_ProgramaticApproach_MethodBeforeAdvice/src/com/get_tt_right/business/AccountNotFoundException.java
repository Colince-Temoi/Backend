package com.get_tt_right.business;
/*NOTES
 -------
 +. Make sure to extend your exception class from RuntimeException predefine class. Reason: 
    - When we throw the child class exception class-AccountNumberNotFoundException- we wont be asked to handle other exceptions that may arise as a result of throwing this child class.

 */
public class AccountNotFoundException extends RuntimeException {

	@Override
	public String toString() {
		return "Invalid Account Number!";
	}
}
