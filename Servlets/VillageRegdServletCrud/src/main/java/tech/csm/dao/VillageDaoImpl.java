package tech.csm.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import tech.csm.entity.Village;
import tech.csm.util.DBUtil;

public class VillageDaoImpl implements VillageDao {

	@Override
	public String saveVillage(Village v) {
//		System.out.println(v);
		
//		Get a session object
		Session ses = DBUtil.getSessionFactory().openSession();
		
//		Begin Transaction
		Transaction tx = ses.beginTransaction();
		
//		Save the Village Object
		ses.persist(v);
		
//		Return success message
		String msg="1 village is added with id: "+ses.getIdentifier(v);
		tx.commit();
		ses.close();
		
		return msg;
	}

}
