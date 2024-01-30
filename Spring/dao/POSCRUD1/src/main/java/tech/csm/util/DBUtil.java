package tech.csm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBUtil {
	
	private static Connection con;
	
	public static Connection getConnection() {
		
		if(con==null) {
			ResourceBundle rb=ResourceBundle.getBundle("system");			
			try {
				con=DriverManager.getConnection(rb.getString("url"),rb.getString("user_name"),rb.getString("password") );
			} catch (SQLException e) {				
				e.printStackTrace();
			}
			return con;
		}
		else
			return con;
	}
	
	public static void closeConnection() {
		if(con!=null) {
			try {
				con.close();
				con=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
