package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import tech.csm.entity.Block;
import tech.csm.util.DBUtil;

public class BlockDaoImpl implements BlockDao {

	@Override
	public List<Block> getAllBlocks() {
		Session ses = DBUtil.getSessionFactory().openSession();
		Query<Block> qr = ses.createQuery("from Block");
		List<Block> blockList=qr.list();
		ses.close();
		return blockList;
	}

}
