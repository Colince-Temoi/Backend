package tech.get_tt_right.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
// Secondary property
	private static Connection con;

//	Behavior to get connection
	public static Connection getConnection() {

		if (con == null) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/empcrud_schema", "root", "Tmi@2022");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return con;

		} else {
			return con;

		}

	}

//	Behavior to close connection
	public static void closeConnection() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con = null;
		}
	}

}
