package com.get_tt_right.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/*bean lifecycle methods implementation; Programmatic approach
 * For our bean class, we need to implement 2 Callback interfaces; InitailizingBean and DisposableBean.
 * Override there respective; afterPropertiesSet() and destroy() methods.
 * These two respective methods are our init() and destroy() methods respectively.
 * 
When actually will this afterPropertiesSet() method execute??
--------------------------------------------------------------
- Yes you are right! haha after properties set. After execution of your respective bean setter methods.
- Container will create objects to your configured beans. Perform Setter DI, then execute the callback interface afterPropertiesSet() method which is our init() method.

We now want to write logic to connect to DB and implement lifecycle init()-afterPropertiesSet() method and destroy methods-destroy() method as well.
-----------------------------------------------------------------------------------------------------------------------------------------------------
- Into this bean I want to maintain the String inputs; driver, url, username, password.
- Inside the lifecycle afterPropertiesSet() method, I am going to write connection logic.
-This afterPropertiesSet method will execute only once during entire lifecyle. When Container is done with object creations of configured beans, it will execute this method.



 * */
/*Callback interfaces-->Means nothing but, just if you implement these interfaces and if you provide implementations for the methods, automatically those methods are going to be executed by Container.
 * We are not going to call this type of methods. Container will take care of everything. Automatically container will call these methods when performing initialization and destruction for the respective bean objects
 * */
public class DaoBean implements InitializingBean, DisposableBean {

	
//  Associations||dependencies||attributes
	private String driver, username, url, password;
	private Connection conn;
	
	
//	Setter methods-To receieve input from Conatiner during Setter DI.
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setUrl(String url) {
		this.url = url;
	}public void setPassword(String password) {
		this.password = password;
	}


//	Lifecycle init() method-Here I am creating Connection object
	@Override
	public void afterPropertiesSet() throws Exception {
		// Writing connection logic
		Class.forName(driver); //Loading driver
		conn = DriverManager.getConnection(url, username, password); // Getting connection.
		System.out.println("Connection Opened");
	}

/*
//  Lifecycle init() method- Here I am creating Connection object.
	public void myInit() throws Exception {
//		Writting Connection logic
		Class.forName(driver);
		conn = DriverManager.getConnection(url, username, password);
		System.out.println("Connection Opened");
		
	}
*/
// Write your Crud methods here.We are going to use our connection here. This Save method here is like a Service||business method.
	public void save(int id, String name, String email, String address) throws Exception {
		PreparedStatement ps = conn.prepareStatement("Insert into Student values(?,?,?,?)");
//		Set your values
		ps.setInt(1, id);
		ps.setString(2, name);
		ps.setString(3, email);
		ps.setString(4, address);
		
		ps.executeUpdate();
		
		System.out.println("Insertion success");
}
	
	
	
//Lifecycle destroy method-Closing DB connection.
	@Override
	public void destroy() throws Exception {
		// Close DB Connection
		conn.close();
		System.out.println("Connection closed");
	}
/*	
public void myDestroy() throws Exception {
//	Close DB connection
	conn.close();
	System.out.println("Connection Closed");
}
*/
}
