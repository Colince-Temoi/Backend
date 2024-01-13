package tech.csm.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import tech.csm.entity.BookRegistration;

public class DBUtil {
	private static SessionFactory sessionFactory;
	static {
		sessionFactory = new Configuration()
				.addAnnotatedClass(BookRegistration.class)

				.buildSessionFactory();
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
