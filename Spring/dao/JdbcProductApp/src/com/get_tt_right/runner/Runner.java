package com.get_tt_right.runner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

//		Get connection
		try ( // Get connection object
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/product_jdbc", "root",
						"Tmi@2022");
				// Get Statement object
				Statement smt = con.createStatement();
//				Execute DRL - Select
				ResultSet rs = smt.executeQuery("SELECT * FROM product_jdbc.t_table;");) {
//			Iterate over the ResultSet

			while (rs.next()) {
				System.out.println("------------------------------------");
				System.out.println(
						rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getDouble(3) + " | " + rs.getDate(4));
				System.out.println("------------------------------------");
			}
//			Execute DML-Insert query
//			int n = smt.executeUpdate("Insert into product_jdbc.t_table (product_id,product_name,unit_price,mfg_date)values(104,'Board',50.00,'2023-03-11')");

//			Taking input from the user
			System.out.println("\n-----------Enter product details to persist---------\n");
			System.out.println("\n-----------Enter product id---------\n");
			int id = sc.nextInt();
			System.out.println("\n-----------Enter product name---------\n");
			String name = sc.next();
			System.out.println("\n-----------Enter product unit price---------\n");
			double price = sc.nextDouble();
			System.out.println("\n-----------Enter product manufacturing date---------\n");
			String date = sc.next();

//			Prepare query String
			String query = "insert into product_jdbc.t_table(product_id,product_name,unit_price,mfg_date)values(" + id
					+ ",'" + name + "'," + price + ",'" + date + "')";

//			System.out.println(query);
//			Execute DML-insert query
			int n = smt.executeUpdate(query);

//			Return appropriate message if success of failure
			System.out.println(n + " record(s) inserted");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
