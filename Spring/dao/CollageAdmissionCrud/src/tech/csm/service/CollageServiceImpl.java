package tech.csm.service;

import java.util.ArrayList;
import java.util.List;

import tech.csm.dao.CollageMasterDao;
import tech.csm.dao.CollageMasterDaoImpl;
import tech.csm.domain.Collage;
import tech.csm.domain.CollageVo;

public class CollageServiceImpl implements CollageService {

//	Primitive dependencies

//	Secondary dependencies
	CollageMasterDao collageMasterDao;

	public CollageServiceImpl() {
		collageMasterDao = new CollageMasterDaoImpl();
	}

	@Override
	public List<CollageVo> getAllCollages() {

//		Invoke dao method
		List<Collage> collage = collageMasterDao.getAllCollages();

		List<CollageVo> collageVo = new ArrayList<>();

//		Convert from Dto to Vo type
		for (Collage collage2 : collage) {
			collageVo.add(covertFroDtoToVo(collage2));
			
		}

		return collageVo;

	}

//	Behavior to convert from Dto to Vo type
	private CollageVo covertFroDtoToVo(Collage collage) {

//		Creating a container to hold Vo type object
		CollageVo collageVo = new CollageVo();

		collageVo.setCollageAddress(collage.getCollageAddress());
		collageVo.setCollageId(collage.getCollageId().toString());
		collageVo.setCollageName(collage.getCollageName());
		collageVo.setNoOfSeats(collage.getNoOfSeats().toString());

		return collageVo;

	}
	@Override
	public CollageVo getCollageById(Integer id) {
	    Collage collage = collageMasterDao.getCollageById(id);

	    // Check if the collage is found
	    if (collage != null) {
	        // Convert from Dto to Vo
	        CollageVo collageVo = covertFroDtoToVo(collage);
	        return collageVo;
	    } else {
	        // Return null if no collage is found
	        return null;
	    }
	}

}
