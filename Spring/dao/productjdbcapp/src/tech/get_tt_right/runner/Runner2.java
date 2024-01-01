package tech.get_tt_right.runner;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Runner2 {

	public static void main(String[] args) throws ParseException {

		String selectByIdQuery = "SELECT product_id,product_name,unit_price,mfg_date FROM productjdbc.t_product where product_id=?";
		String selectAllQuery = "SELECT product_id,product_name,unit_price,mfg_date FROM productjdbc.t_product where unit_price>?";
		String countQuery = "select count(*) from productjdbc.t_product where unit_price>?";
		String insertQuery = "INSERT INTO productjdbc.t_product (product_name, unit_price, mfg_date) VALUES (?, ?, ?)";

//		get Db connection
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/productjdbc", "root",
				"Tmi@2022");
				Scanner sc = new Scanner(System.in);
				PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement psCount = con.prepareStatement(countQuery);

		) {
//			Test if connection is established
//			System.out.println(con);

			/*
			 * // Drl - Read one record by id!
			 * 
			 * System.out.println("Enter id to product you want outputed"); Integer id =
			 * sc.nextInt();
			 * 
			 * // Replace the question mark with a real value now. ps.setInt(1, id);
			 * 
			 * // Invoking the DRL method on top of ps reference ResultSet rs =
			 * ps.executeQuery();
			 * 
			 * while (rs.next()) {
			 * System.out.println(rs.getInt(1)+" | "+rs.getString(2)+" | "+rs.getDouble(3)
			 * +" | "+rs.getString(4)); }
			 */

//			DML - Insert record(s)

			char option;
			Integer numOfInsertedRecords = 0;
			System.out.println("-------Insert Records into the t_product table-----\n");
			do {

//				Take inputs from the user

				System.out.println("Enter product_name");
				String name = sc.next();
				System.out.println("Enter unit_price");
				Double price = sc.nextDouble();
				System.out.println("Enter mfg_date[yyyy-mm-dd]");
				String date = sc.next();

//				1. Set the values
				ps.setString(1, name);
				ps.setDouble(2, price);
				ps.setDate(3, new Date(new SimpleDateFormat("yyy-MM-dd").parse(date).getTime()));

//				2. Fire the executeUpdate method of PS
				Integer count = ps.executeUpdate();
				numOfInsertedRecords += count;
				
//				Get the generated key after the insert is successful
				ResultSet key = ps.getGeneratedKeys();
				
				key.next();
				

				System.out.println("\n"+count+" Record inserted sucessfully with the id "+key.getInt(1)+"\n");

				System.out.println("Do you want to insert another record![y/n]");
				option = sc.next().charAt(0);
			} while (option == 'y');

			System.out.println(numOfInsertedRecords + " record(s) inserted successfully!!");

			// Drl - Read all records!

			/*
			 * System.out.
			 * println("Enter the unit price to that you want match the selection criteria"
			 * ); Double price = sc.nextDouble();
			 * 
			 * // Replace the question mark with a real value now. ps.setDouble(1, price);
			 * psCount.setDouble(1, price);
			 * 
			 * // Invoking the DRL method on top of ps reference ResultSet rs =
			 * ps.executeQuery(); ResultSet rsCount = psCount.executeQuery();
			 * 
			 * Integer count = 0;
			 * 
			 * while (rsCount.next()) { count = rsCount.getInt(1);
			 * 
			 * }
			 * 
			 * if (rs.next()) {
			 * System.out.println("\n-----"+count+" records found!----------- \n"); do {
			 * System.out.println( rs.getInt(1) + " | " + rs.getString(2) + " | " +
			 * rs.getDouble(3) + " | " + rs.getString(4)); } while (rs.next()); } else {
			 * System.out.println("\n-------"+count+" record(s) found with price > "+price+
			 * "---------\n"); }
			 */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
