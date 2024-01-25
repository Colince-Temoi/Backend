package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import tech.csm.entity.County;
import tech.csm.util.DBUtil;

public class CountyDaoImpl implements CountyDao {

	@Override
	public List<County> getAllCounties() {
		Session ses = DBUtil.getSessionFactory().openSession();
		Query<County> qr = ses.createQuery("from County");
		List<County> countyList=qr.list();
		ses.close();
		return countyList;
	}

}
