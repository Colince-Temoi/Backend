package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import tech.csm.entity.Panchayat;
import tech.csm.util.DBUtil;

public class PanchayatDaoImpl implements PanchayatDao {

	@Override
	public List<Panchayat> getPanchayatByBlockId(Integer blockId) {
		final String seQuery="from Panchayat p where p.block.blockId=:bid";
		Session ses = DBUtil.getSessionFactory().openSession();
		Query<Panchayat> qr = ses.createQuery(seQuery);
		qr.setParameter("bid", blockId);
		List<Panchayat> panchayatList=qr.list();
		ses.close();
		return panchayatList;
	}

}
