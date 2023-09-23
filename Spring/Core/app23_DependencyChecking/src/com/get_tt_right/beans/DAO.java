package com.get_tt_right.beans;

import java.sql.Connection;
import java.sql.DriverManager;
/* @Required annotation Dependency checking.
 * Is deprecated as of Spring 5.1. It is no longer recommended to use the @Required annotation.
 * Instead, you should use constructor injection to inject required dependencies.
 * This is the reason am not implementing this concept here; For knowledge purpose just visit the notes if you need to know an=bout it.
 * */
public class DAO {


//Attributes||Associations||dependencies||Properties
	private String driver;
	private String url;
	private String username;
	private String password;

//Setter && getter methods.

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	//Business methods
		public void printConnection() throws Exception {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,username,password);
			System.out.println(con);
		}

}
