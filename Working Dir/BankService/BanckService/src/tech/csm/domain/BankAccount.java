package tech.csm.domain;

import tech.csm.util.BankUtil;

public class BankAccount {
	private String accountNumber;
	private String depositorName;
	private String address;
	private String accountType;
	private double accountBalance;
	private int noOfTransaction;
	public BankAccount(String depositorName, String address, String accountType, double accountBalance) {
		super();
		this.accountNumber=BankUtil.generateAccountNo();
		this.depositorName = depositorName;
		this.address = address;
		this.accountType = accountType;
		this.accountBalance = accountBalance;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public double getAccountBalance() {
		return accountBalance;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void deposite(double amount) {
		this.accountBalance+=amount;
		this.noOfTransaction++;
	}
	public void withdraw(double amount) {
		this.accountBalance-=amount;
		this.noOfTransaction++;
	}
	
	public String getAccoutInfo() {
		return "BankAccount [accountNumber=" + accountNumber + ", depositorName=" + depositorName + ", address="
				+ address + ", accountType=" + accountType + ", accountBalance=" + accountBalance + ", noOfTransaction="
				+ noOfTransaction + "]";
	}	
	
}
