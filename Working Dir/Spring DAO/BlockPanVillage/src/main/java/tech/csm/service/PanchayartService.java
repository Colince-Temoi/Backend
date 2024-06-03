package tech.csm.service;

import java.util.List;

import tech.csm.entity.Panchayart;

public interface PanchayartService {

	List<Panchayart> getPanchayartByBlockId(Integer blockId);

}
