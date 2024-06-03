package tech.csm.dao;

import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import tech.csm.entity.Village;
import tech.csm.util.DBUtil;

public class VillageDaoImpl implements VillageDao {

	@Override
	public String addVillage(Village village) {
		Session ses = DBUtil.getSessionFactory().openSession();
		Transaction tx = ses.beginTransaction();
		ses.save(village);
		tx.commit();
		ses.close();
		return "Village added successfully!";
	}

	@Override
	public List<Village> getAllVillages() {
		Session ses = DBUtil.getSessionFactory().openSession();
		Query<Village> sequery = ses.createQuery("from Village");
		List<Village> villagelist = sequery.list();
		return villagelist;
	}

}
