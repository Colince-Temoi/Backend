package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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

	@Override
	public List<Village> getAllVillages() {
		
//		Prepare query
		final String seQuery="from Village";
		
//		Get session object
		Session ses = DBUtil.getSessionFactory().openSession();
		
//		Execute query
		Query<Village> qr = ses.createQuery(seQuery);
		
//		Return a list of Villages
		List<Village> villageList=qr.list();
		
		
		ses.close();
		return villageList;
	}

}
