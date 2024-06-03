package tech.csm.runner;

import java.util.Scanner;

import tech.csm.domain.BankAccount;

public class Runner {

	public static void main(String[] args) {
		BankAccount[] accounts = new BankAccount[5];
		Scanner sc = new Scanner(System.in);
		Scanner ss = new Scanner(System.in);
		int choice, i;
		int count = 0;
		double na;
		do {
			System.out.println("\n1.Create Account\n2.Deposite\n"
					+ "3.Withdraw\n4.Change Address\n5.Account Info\n6.Exit" + "\nEnter your choice[1..6]:");
			choice = sc.nextInt();
			switch (choice) {
			case 1:
				System.out.println("Enter name,address, account type(current/savings), account balance:");
				accounts[count] = new BankAccount(ss.nextLine(), ss.nextLine(), ss.nextLine(), sc.nextDouble());
				System.out.println("Account created with account no" + accounts[count++].getAccountNumber());
				break;
			case 2:
				System.out.println("Enter the account number:");
				String ac = ss.nextLine();
				System.out.println("Enter the amount for deposite");
				na = sc.nextDouble();

				for (i = 0; i < accounts.length; i++) {
					if (accounts[i].getAccountNumber().equalsIgnoreCase(ac)) {
						accounts[i].deposite(na);
						break;
					}
				}
				if (i == accounts.length)
					System.out.println("Account does not exist, plz check acc no");
				else
					System.out.println("Deposite succesfull, check account balance");

				break;
			case 3:

				System.out.println("Enter the account number:");
				ac = ss.nextLine();
				System.out.println("Enter the amount for widtdraw");
				na = sc.nextDouble();

				for (i = 0; i < accounts.length; i++) {
					if (accounts[i].getAccountNumber().equalsIgnoreCase(ac)) {
						if (accounts[i].getAccountBalance() >= na) {
							accounts[i].withdraw(na);
							System.out.println("Withdrawl succesfull, check account balance");
							
						} else 
							System.out.println("insufficient fund!!! check account balance");
						break;
					}
							
				}
				if (i == accounts.length)
					System.out.println("Account does not exist, plz check acc no");

				break;
			case 4:
				System.out.println("Enter the account number:");
				ac = ss.nextLine();
				System.out.println("Enter new address");
				String nadd = ss.nextLine();
				for (i = 0; i < accounts.length; i++) {
					if (accounts[i].getAccountNumber().equalsIgnoreCase(ac)) {
						accounts[i].setAddress(nadd);
						System.out.println("Address updated successfully");
						break;
					}
					
				}
				if (i == accounts.length)
					System.out.println("Account does not exist, plz check acc no");

				break;
			case 5:
				System.out.println("Enter the account number:");
				ac = ss.nextLine();
				for (i = 0; i < accounts.length; i++) {
					if (accounts[i].getAccountNumber().equalsIgnoreCase(ac)) {
						System.out.println(accounts[i].getAccoutInfo());
						break;
					}
				}
				if (i == accounts.length)
					System.out.println("Account does not exist, plz check acc no");

				break;
			case 6:
				break;
			default:
				System.out.println("invlaid choice!!!!");

			}

		} while (choice != 6);

	}

}
