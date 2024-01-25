package tech.csm.util;



import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import tech.csm.entity.County;
import tech.csm.entity.Constituency;
import tech.csm.entity.Village;





public class DBUtil {
	private static SessionFactory sessionFactory;
	static {	
		sessionFactory=new Configuration()
				.addAnnotatedClass(County.class)	
				.addAnnotatedClass(Constituency.class)
				.addAnnotatedClass(Village.class)
				.buildSessionFactory();
	}
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	
}
