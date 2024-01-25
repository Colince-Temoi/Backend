package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import tech.csm.entity.Constituency;
import tech.csm.util.DBUtil;

public class ConstituencyDaoImpl implements ConstituencyDao {

	@Override
	public List<Constituency> getConstituencieByCountyId(Integer countyId) {
		final String seQuery="from Constituency c where c.county.countyId=:cId";
		Session ses = DBUtil.getSessionFactory().openSession();
		Query<Constituency> qr = ses.createQuery(seQuery);
		qr.setParameter("cId", countyId);
		List<Constituency> constituencies=qr.list();
		ses.close();
		return constituencies;
	}

}
