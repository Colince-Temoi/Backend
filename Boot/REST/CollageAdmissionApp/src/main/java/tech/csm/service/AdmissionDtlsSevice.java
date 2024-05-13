package tech.csm.service;

import java.util.List;
import java.util.Map;

import tech.csm.domain.AdmissionDtls;

public interface AdmissionDtlsSevice {

	String saveAdmissionDtls(AdmissionDtls admissionDtls);

//	List<AdmissionDtls> getAllAdmissionDetatils();
	List<Map<String, Object>> fetchAllAdmissionDetails();

	String cancelAdmission(int id);

}
