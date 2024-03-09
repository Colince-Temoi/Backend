package tech.csm.dao;

import java.util.List;

import tech.csm.domain.Collage;

public interface Collagedao {

	List<Collage> findAllCollages();

	Collage getCollageById(Integer collageId);

}
