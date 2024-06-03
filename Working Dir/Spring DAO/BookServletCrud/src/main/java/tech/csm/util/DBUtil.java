package tech.csm.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBUtil {
	private static SessionFactory sessionFactory;
	static {
		sessionFactory = new Configuration()

				.buildSessionFactory();
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
