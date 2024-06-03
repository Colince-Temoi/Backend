package tech.csm.dao;

import java.util.List;

import tech.csm.entity.Panchayat;

public interface PanchayatDao {

	List<Panchayat> getPanchayatByBlockId(Integer blockId);

}
