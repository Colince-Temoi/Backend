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
		Session ses = DBUtil.getSessionFactory().openSession();
		Transaction tx = ses.beginTransaction();
		ses.persist(v);
		String msg="1 village is added with id: "+ses.getIdentifier(v);
		tx.commit();
		ses.close();
		
		return msg;
	}

	@Override
	public List<Village> getAllVillages(int pageNo, int pageSize) {
		final String seQuery="from Village";
		Session ses = DBUtil.getSessionFactory().openSession();
		
		Query<Village> qr = ses.createQuery(seQuery);
		System.out.println("pageSize"+pageSize+"  "+"pageNo="+pageNo+" *******");
		qr.setFirstResult(pageNo*pageSize);
		qr.setMaxResults(pageSize);
		List<Village> villageList=qr.list();
		
		
		ses.close();
		return villageList;
	}

	@Override
	public List<Village> getVillageByBlockId(Integer bId) {
		final String seQuery="from Village v where v.panchayat.block.blockId=:bid";
		Session ses = DBUtil.getSessionFactory().openSession();
		Query<Village> qr = ses.createQuery(seQuery);
		qr.setParameter("bid", bId);
		List<Village> villageList=qr.list();
		ses.close();
		return villageList;
	}

	@Override
	public Long getTableSize() {
		final String seQuery="select count(*) from Village";
		Session ses = DBUtil.getSessionFactory().openSession();
		Query<Long> qr = ses.createQuery(seQuery);		
		Long nr=qr.uniqueResult();
		ses.close();
		return nr;
	}

	@Override
	public List<Village> getAllVillages() {
		final String seQuery="from Village";
		Session ses = DBUtil.getSessionFactory().openSession();
		
		Query<Village> qr = ses.createQuery(seQuery);		
		List<Village> villageList=qr.list();
		
		
		ses.close();
		return villageList;
	}

}
