package tech.csm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import tech.csm.dao.AdmissionDao;
import tech.csm.dao.AdmissionDaoImpl;
import tech.csm.domain.AdmissionDetails;
import tech.csm.domain.AdmissionDetailsVo;
import tech.csm.domain.Collage;

public class AdmissionDetailsServiceImpl implements AdmissionDetailsService {
	
//	Primitive dependencies
	
//	Secondary dependencies
	private AdmissionDao admissionDao;
	
	public AdmissionDetailsServiceImpl() {
		admissionDao = new AdmissionDaoImpl();
	}
	

	@Override
	public String saveCandidateAdmissionDetails(AdmissionDetailsVo admissionDetailsVo) {

//		Convert Vo object to Dto for persistance
		AdmissionDetails admissionDetails = convertFromVoToDto(admissionDetailsVo);
		
//		Invoke dao method to persist the admission details
		
		String message = admissionDao.saveAdmissionDetails(admissionDetails);
		
		return message;
	}
	
//	Convert from Vo to Dto behavior
	private AdmissionDetails convertFromVoToDto(AdmissionDetailsVo admissionDetailsVo) {

//		Create a storage container to store Dto admission details.
		AdmissionDetails admissionDetails = new AdmissionDetails();
		
		try {
			admissionDetails.setAdmission_date(new SimpleDateFormat("yyyy-MM-dd").parse(admissionDetailsVo.getAdmission_date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		admissionDetails.setAdmissionId(Integer.parseInt(admissionDetailsVo.getAdmissionId()));
		admissionDetails.setCandidateAddress(admissionDetailsVo.getCandidateAddress());
		admissionDetails.setCandidateName(admissionDetailsVo.getCandidateName());
		admissionDetails.setCandidatePhone(admissionDetailsVo.getCandidatePhone());
		
//		Convert Collage related details as wel from Vo type to dto type
//		Create a storage container to store Dto admission details.
		Collage collage = new Collage();
		
		collage.setCollageAddress(admissionDetailsVo.getCollageVo().getCollageAddress());
		collage.setCollageId(Integer.parseInt(admissionDetailsVo.getCollageVo().getCollageId()));
		collage.setCollageName(admissionDetailsVo.getCollageVo().getCollageName());
		collage.setNoOfSeats(Integer.parseInt(admissionDetailsVo.getCollageVo().getNoOfSeats()));
		
		admissionDetails.setCollage(collage);
		
		
		return admissionDetails;
		
	}


	@Override
	public String cancelAdmissionById(int admissionId) {
	    // Invoke dao method to cancel the admission by ID
	    String message = admissionDao.cancelAdmissionById(admissionId);
	    return message;
	}



	@Override
	public String modifyAdmissionById(int admissionId, AdmissionDetailsVo modifiedAdmissionDetails) {
	    // Convert Vo object to Dto for persistence
	    AdmissionDetails modifiedAdmission = convertFromVoToDto(modifiedAdmissionDetails);
	    
	    // Invoke dao method to modify the admission by ID
	    String message = admissionDao.modifyAdmissionById(admissionId, modifiedAdmission);
	    
	    return message;
	}


	@Override
	public List<AdmissionDetailsVo> getAdmissionReport() {
	    // Invoke dao method to get the list of admission details
	    List<AdmissionDetails> admissionList = admissionDao.getAdmissionDetails();
	    
	    // Convert the list of Dto to Vo for presentation
	    List<AdmissionDetailsVo> admissionVoList = convertFromDtoToVo(admissionList);
	    
	    return admissionVoList;
	}






}
