package com.get_tt_right.exception;

public class AccountNumberNotFoundException extends RuntimeException {

	@Override
	public String toString() {
		return "Invalid account number!! Enter correct account Number!";
	}
}
