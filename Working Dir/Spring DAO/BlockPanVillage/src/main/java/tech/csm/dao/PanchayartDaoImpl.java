package tech.csm.dao;

import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.Session;

import tech.csm.entity.Panchayart;
import tech.csm.util.DBUtil;

public class PanchayartDaoImpl implements PanchayartDao {

	@Override
	public List<Panchayart> getPanchayartByBlockId(Integer blockId) {
		Session ses = DBUtil.getSessionFactory().openSession();
		final String sequery = "from Panchayart p where p.block.blockId=:blockId";
		Query qr = ses.createQuery(sequery);
		qr.setParameter("blockId", blockId);
		List<Panchayart> panchayartList = qr.list();
		ses.close();
		return panchayartList;
	}

}
