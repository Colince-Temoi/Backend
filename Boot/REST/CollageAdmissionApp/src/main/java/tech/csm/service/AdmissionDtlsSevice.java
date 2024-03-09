package tech.csm.service;

import java.util.List;

import tech.csm.domain.AdmissionDtls;

public interface AdmissionDtlsSevice {

	String saveAdmissionDtls(AdmissionDtls admissionDtls);

	List<AdmissionDtls> getAllAdmissionDetatils();

	String cancelAdmission(int id);

}
