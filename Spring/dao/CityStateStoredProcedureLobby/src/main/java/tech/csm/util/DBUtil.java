package tech.csm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBUtil {
	
	    private static Connection con;
	
	    private static final String DB_URL = "jdbc:mysql://localhost:3306/city_state_storedprocedure_lobby"; 
	    private static final String USERNAME = "root";
	    private static final String PASSWORD = "Tmi@2022";

	    public static Connection getConnection() throws SQLException {
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL JDBC driver
	            con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
	            return con;
	        } catch (ClassNotFoundException ex) {
	            System.err.println("MySQL JDBC Driver not found.");
	            throw new SQLException(ex);
	        }
	    }

	    public static void closeConnection() {
	        if (con != null) {
	            try {
	                con.close();
	                con=null;
	            } catch (SQLException ex) {
	                System.err.println("Error closing connection: " + ex.getMessage());
	            }
	        }
	    }
	
//	private static Connection con;
//	
//	public static Connection getConnection() {
//		
//		if(con==null) {
//			ResourceBundle rb=ResourceBundle.getBundle("system");			
//			try {
//				con=DriverManager.getConnection(rb.getString("url"),rb.getString("user_name"),rb.getString("password") );
//			} catch (SQLException e) {				
//				e.printStackTrace();
//			}
//			return con;
//		}
//		else
//			return con;
//	}
//	
//	public static void closeConnection() {
//		if(con!=null) {
//			try {
//				con.close();
//				con=null;
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	
}
