package com.get_tt_right.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class RetrievingConnectionsPlainJdbc {

	public static void main(String[] args) {
		
		Date start = new Date();
		
//		Retrieving 200k connection- Plain Jdbc
		for (int i = 0; i < 200000; i++) {
			try {
//				Loading Driver
				Class.forName("oracle.jdbc.OracleDriver");

//			Create Connection
				try {
					Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521", "system",
							"system");
					System.out.println(con + " : " + i);
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		Date endingTime = new Date();
		System.out.println("Start time: "+start);
		System.out.println("end time: "+endingTime);

	}

}
/*It is a bit slower.
 * It will not open and close connection like it would have for Java resources.
 * It will take some time.
 * */
