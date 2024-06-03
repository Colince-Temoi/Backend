package tech.csm.service;

import java.util.List;

import tech.csm.dao.BlockDao;
import tech.csm.dao.BlockDaoImpl;
import tech.csm.entity.Block;

public class BlockServiceImpl implements BlockkService {
private BlockDao blockDao= new BlockDaoImpl();
	
	@Override
	public List<Block> getAllBlocks() {
		// TODO Auto-generated method stub
		return blockDao.getAllBlock();
	}

}
