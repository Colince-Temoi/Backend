package tech.csm.dao;

import java.util.List;

import tech.csm.entity.Panchayart;

public interface PanchayartDao {

	List<Panchayart> getPanchayartByBlockId(Integer blockId);

}
