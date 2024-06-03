package tech.csm.util;



import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import tech.csm.entity.Block;
import tech.csm.entity.Panchayat;
import tech.csm.entity.Village;





public class DBUtil {
	private static SessionFactory sessionFactory;
	static {	
		sessionFactory=new Configuration()
				.addAnnotatedClass(Block.class)	
				.addAnnotatedClass(Panchayat.class)
				.addAnnotatedClass(Village.class)
				.buildSessionFactory();
	}
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	
}
