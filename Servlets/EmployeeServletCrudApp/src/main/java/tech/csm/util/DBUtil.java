package tech.csm.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import tech.csm.entity.Departments;
import tech.csm.entity.Employees;

public class DBUtil {
	private static SessionFactory sessionFactory;
	static {
		sessionFactory = new Configuration()
				.addAnnotatedClass(Departments.class)
				.addAnnotatedClass(Employees.class)

				.buildSessionFactory();
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
