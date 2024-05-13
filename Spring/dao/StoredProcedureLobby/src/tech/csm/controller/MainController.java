package tech.csm.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;

import tech.get_tt_right.util.DbUtil;

public class MainController {

//	Secondary dependency
	private static Connection con;

	public MainController() {
		con = DbUtil.getConnection();
	}

	public static void main(String[] args) {
//		Invoke constructor to initialize the things
		new MainController();

//		System.out.println(con);

		/*
		 * try { CallableStatement cs =
		 * con.prepareCall("{call pos_schema.products(?, ?, ?)}");
		 * 
		 * // Set all IN parameters with values cs.setInt(1, 2);
		 * 
		 * // Register OUT parameters cs.registerOutParameter(2, Types.DOUBLE);
		 * cs.registerOutParameter(3, Types.VARCHAR);
		 * 
		 * // Execute the things cs.execute();
		 * 
		 * // Access the values from the OUT parameters
		 * System.out.println(cs.getDouble(2)+" +++ "+cs.getString(3));
		 * 
		 * } catch (SQLException e) {
		 * 
		 * e.printStackTrace(); }
		 */

//		Invoke methods
		select();
//		insert();

	}

	private static void select() {
		try {
			// do not write semicolon in-front of the } when invoking the stored procedure
			// i.e.,"{call pos_schema.p_productscreen(?, ?,?,?,?,?);}" as you may encounter
			// errors at run-time
			CallableStatement cs = con.prepareCall("{call pos_schema.p_productscreen(?,?, ?,?,?,?)}");

//			Set to in parameters with values
			cs.setString(1, "se");
			cs.setInt(3, Types.NULL);
			cs.setString(4, null);
			cs.setInt(5, Types.NULL);
			cs.setDouble(6, Types.NULL);

//			register out parameters
			cs.registerOutParameter(2, Types.VARCHAR);

//			Execute the things
			ResultSet rs = cs.executeQuery();

//			Access out values
			System.out.println(cs.getString(2));
			
			if (rs.next()) {
				do {
					System.out.println("|| "+rs.getInt(1)+" || "+rs.getString(2)+" || "+rs.getDouble(4)+" || "+rs.getInt(3));
					
				} while (rs.next());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insert() {
		try {
			// do not write semicolon in-front of the } when invoking the stored procedure
			// i.e.,"{call pos_schema.p_productscreen(?, ?,?,?,?,?);}" as you may encounter
			// errors at run-time
			CallableStatement cs = con.prepareCall("{call pos_schema.p_productscreen(?, ?,?,?,?,?)}");

//			Set to in parameters with values
			cs.setString(1, "in");
			cs.setInt(3, 7);
			cs.setString(4, "Counter Book");
			cs.setInt(5, 89);
			cs.setDouble(6, 70);

//			register out parameters
			cs.registerOutParameter(2, Types.VARCHAR);

//			Execute the things
			cs.execute();

//			Access out values
			System.out.println(cs.getString(2));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
