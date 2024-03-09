package tech.csm.service;

import java.util.List;

import tech.csm.domain.AdmissionDetailsVo;

public interface AdmissionDetailsService {

	String saveCandidateAdmissionDetails(AdmissionDetailsVo admissionDetailsVo);

	String cancelAdmissionById(int admissionId);

	String modifyAdmissionById(int admissionId, AdmissionDetailsVo modifiedAdmissionDetails);

	List<AdmissionDetailsVo> getAdmissionReport();

}
