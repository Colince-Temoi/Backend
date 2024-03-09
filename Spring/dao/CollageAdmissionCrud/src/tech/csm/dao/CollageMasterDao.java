package tech.csm.dao;

import java.util.List;

import tech.csm.domain.Collage;

public interface CollageMasterDao {

	List<Collage> getAllCollages();

	Collage getCollageById(Integer id);

}
