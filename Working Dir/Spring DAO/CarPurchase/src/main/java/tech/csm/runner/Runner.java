package tech.csm.runner;

import org.hibernate.Session;

import tech.csm.util.DBUtil;

public class Runner {

	public static void main(String[] args) {
		Session ses=DBUtil.getSessionFactory().openSession();
		
		System.out.println(ses);

	}

}
