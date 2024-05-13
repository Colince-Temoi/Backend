package tech.csm.controller;

import java.util.Scanner;

import tech.csm.util.DBUtil;

public class AnotherController {

	private static final Scanner scanner = new Scanner(System.in);

	 public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	        boolean exit = false;

	        while (!exit) {
	        	System.out.println("\n1.Add City Inf\n2.Show All Cities) \n" + "3.Delete City By City Id\n"
						+ "4.Update City By City Id\n" + "5.Exit\n" + "Enter your Choice : ");
	            int choice = scanner.nextInt();
	            scanner.nextLine(); // Consume newline

	            switch (choice) {
	                case 1:
	                	System.out.println();
	                	System.out.println(" Inside Add City");
	                    // Add City (Call your DAO method here)
	                    break;
	                case 2:
	                    // Show all cities in a given state (Call your DAO method here)
	                    break;
	                case 3:
	                    // Delete City By Id (Call your DAO method here)
	                    break;
	                case 4:
	                    // Update City Id (Call your DAO method here)
	                    break;
	                case 5:
	                    System.out.print("Are you sure you want to exit? [y/n]: ");
	                    if (scanner.nextLine().equalsIgnoreCase("y")) {
	                        DBUtil.closeConnection(/* Pass connection here */); 
	                        exit = true;
	                    }
	                    break;
	                default:
	                    System.out.println("Invalid Choice");
	            }
	        }

	        scanner.close();

	    }
}
