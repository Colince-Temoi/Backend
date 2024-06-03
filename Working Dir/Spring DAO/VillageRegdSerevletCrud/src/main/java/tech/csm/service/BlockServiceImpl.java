package tech.csm.service;

import java.util.List;

import tech.csm.dao.BlockDao;
import tech.csm.dao.BlockDaoImpl;
import tech.csm.entity.Block;

public class BlockServiceImpl implements BlockService {

	private BlockDao blockDao;
	public BlockServiceImpl() {
		blockDao=new BlockDaoImpl();
	}
	
	@Override
	public List<Block> getAllBlocks() {
		
		return blockDao.getAllBlocks();
	}

}
