package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.dao.AdmissionDtlsDao;
import tech.csm.dao.Collagedao;
import tech.csm.domain.AdmissionDtls;
import tech.csm.domain.Collage;

@Service
public class AdmissionDtlsServiceImpl implements AdmissionDtlsSevice {

	@Autowired
	private AdmissionDtlsDao admissionDtlsDao;
	
	@Autowired
	private Collagedao collageDao;
	
	@Override
	public String saveAdmissionDtls(AdmissionDtls admissionDtls) {
		return admissionDtlsDao.saveAdmissionDtls(admissionDtls);
	}

	@Override
	public List<AdmissionDtls> getAllAdmissionDetatils() {
		List<AdmissionDtls> admnDtlList = admissionDtlsDao.getAllAdmissionDtls();
		
		for (AdmissionDtls x : admnDtlList) {
			Collage collage = getCollageById(x.getCollage().getCollageId());
			x.setCollage(collage);
		}
		return admnDtlList;
	}

	private Collage getCollageById(Integer collageId) {
		
		Collage collage = collageDao.getCollageById(collageId);
		return collage;
		
	}

	@Override
	public String cancelAdmission(int id) {
		return admissionDtlsDao.cancelAdmission(id);
	}

}
