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
		String msg = "1 village is added with id: " + ses.getIdentifier(v);
		tx.commit();
		ses.close();

		return msg;
	}

	@Override
	public List<Village> getAllVillages() {

		// Prepare query final
		String seQuery = "from Village";

		// Get session object
		Session ses = DBUtil.getSessionFactory().openSession();

		// prepare the query: Just like PS object
		Query<Village> qr = ses.createQuery(seQuery);

		// Return a list of Villages on top of qr object
		List<Village> villageList = qr.list();

		ses.close();
		return villageList;
	}

	@Override
	public List<Village> getAllVillages(Integer pageNo, Integer pageSize) {
//		Prepare query
		final String seQuery = "from Village";

//		Get session object
		Session ses = DBUtil.getSessionFactory().openSession();

//		prepare the query: Just like PS object
		Query<Village> qr = ses.createQuery(seQuery);

//		Write the below 2 lines before executing the query
		qr.setFirstResult(pageNo * pageSize);
		qr.setMaxResults(pageSize);

//		Return a list of Villages on top of qr object
		List<Village> villageList = qr.list();

		ses.close();
		return villageList;
	}

	@Override
	public List<Village> getVillageByBlockId(Integer cId) {
//		Prepare query String
		final String seQuery = "from Village v where v.constituency.county.countyId=:cid";

//		Get session object
		Session ses = DBUtil.getSessionFactory().openSession();

//		prepare the query: Just like PS object
		Query<Village> qr = ses.createQuery(seQuery);

//		Set values into the query placeholder(s)
		qr.setParameter("cid", cId);

//		Return a list of Villages on top of qr object
		List<Village> villageList = qr.list();

		ses.close();
		return villageList;
	}

	@Override
	public Long getTableSize() {
//		Prepare a query to get the # of records in table
		final String seQuery = "select count(*) from Village";

//		Get Session object
		Session ses = DBUtil.getSessionFactory().openSession();

//		ready the query: just like PS in JDBC
		Query<Long> qr = ses.createQuery(seQuery);
//		Execute the query
		Long nr = qr.uniqueResult();
		ses.close();
		return nr;
	}

}
