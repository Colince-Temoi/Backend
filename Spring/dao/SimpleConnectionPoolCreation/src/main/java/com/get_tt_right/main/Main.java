package com.get_tt_right.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.dbcp2.BasicDataSource;

public class Main {

	public static void main(String[] args) throws SQLException {

//		Create BasicDataSource object
		BasicDataSource bds = new BasicDataSource();

//		Invoke BDS setter methods
		bds.setDriverClassName("oracle.jdbc.OracleDriver");
//		For Multitenant Container DB: Working for only system user
		bds.setUrl("jdbc:oracle:thin:@//localhost:1521");
		
//		For Pluggable DB: Working as well for the users: system and pdbadmin
//		bds.setUrl("jdbc:oracle:thin:@//localhost:1521/XEPDB1");
		bds.setUsername("system");
//		bds.setUsername("pdbadmin");
		bds.setPassword("system");
		
//		I have found out that the max number of connections to my Oracle DB are 256.
		bds.setMaxTotal(258);
		bds.setMinIdle(10);
		bds.setMaxWaitMillis(1000*5);
		
//		Connection con = bds.getConnection();
//		System.out.println(con);

		Date start = new Date();
//		Trying to retrieve 100k connections
		for (int i = 0; i < 200000; i++) {
			try {
				Connection con1 = bds.getConnection();
				System.out.println(con1+" : "+i);
				con1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Date endingTime = new Date();
		System.out.println("Start time: "+start);
		System.out.println("end time: "+endingTime);
	}

}
/*Retrieving Connections from Pool
 * Here; interaction between pool and our Java code is Java to Java interaction. Our Java Code is Interacting with CP Map object.
 * The interaction is not between Our Java code and DB(native machine)
 * Hence; 100k connections it will retrieve and close close within few seconds-12sec
 * */
