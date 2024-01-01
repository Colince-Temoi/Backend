package tech.get_tt_right.runner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {

//		Get connection object
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/productjdbc", "root",
				"Tmi@2022");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("Select * from productjdbc.t_product");

				Scanner sc = new Scanner(System.in);

		) {
			int count = 0;

			/*
			 * // Update operation - criteria: by id
			 * System.out.println("Enter id to the product you want to update"); Integer id
			 * = sc.nextInt();
			 * 
			 * System.out.println("Enter product_name"); String name = sc.next();
			 * System.out.println("Enter unit_price"); Double price = sc.nextDouble();
			 * System.out.println("Enter mfg_date[yyyy-mm-dd]"); String date = sc.next();
			 * 
			 * String updatequery =
			 * "UPDATE productjdbc.t_product SET product_name ='"+name+"',unit_price ="
			 * +price+", mfg_date ='" +date+"' WHERE (product_id ="+id+")";
			 * 
			 * count = stmt.executeUpdate(updatequery);
			 * System.out.println(count+" record(s) updated!");
			 */
			
			/*
			 * // Delete operation - criteria: by id.
			 * System.out.println("Enter id to the product you want to delete"); Integer id
			 * = sc.nextInt();
			 * 
			 * String delquery = "DELETE FROM productjdbc.t_product WHERE product_id =" +
			 * id;
			 * 
			 * count = stmt.executeUpdate(delquery);
			 * 
			 * System.out.println(count + " record(s) deleted!");
			 */

			/*
			 * // Insert operation // Take input from user
			 * System.out.println("Enter product id"); Integer id = sc.nextInt();
			 * 
			 * System.out.println("Enter product_name"); String name = sc.next();
			 * 
			 * System.out.println("Enter unit_price"); Double price = sc.nextDouble();
			 * 
			 * System.out.println("Enter mfg_date[yyyy-mm-dd]"); String date = sc.next();
			 * 
			 * // Prepare query String String insertQuery =
			 * "INSERT INTO productjdbc.t_product (product_id, product_name, unit_price, mfg_date) VALUES ("
			 * + id + ",'" + name + "'," + price + ",'" + date + "')";
			 * 
			 * // System.out.println(con); // o/p - com.mysql.cj.jdbc.ConnectionImpl@8f2ef19
			 * 
			 * Integer i = stmt.executeUpdate(insertQuery);
			 * System.out.println(i+" record(s) inserted!")
			 */;

//			 DRL operation

//			Iterating over ResultSet object.
			/*
			 * while (rs.next()) { System.out.println("| " + rs.getInt(1) + "| " +
			 * rs.getString(2) + "| " + rs.getDouble(3) + "| " + rs.getDate(4) + "|"); }
			 */

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
