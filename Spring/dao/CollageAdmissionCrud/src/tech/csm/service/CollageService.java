package tech.csm.service;

import java.util.List;

import tech.csm.domain.CollageVo;

public interface CollageService {

	List<CollageVo> getAllCollages();

	CollageVo getCollageById(Integer id);

}
