package tech.csm.dao;

import java.util.List;

import tech.csm.domain.AdmissionDetails;

public interface AdmissionDao {

	String saveAdmissionDetails(AdmissionDetails admissionDetails);

	String cancelAdmissionById(int admissionId);

	String modifyAdmissionById(int admissionId, AdmissionDetails modifiedAdmission);

	List<AdmissionDetails> getAdmissionDetails();

}
