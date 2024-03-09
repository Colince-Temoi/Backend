package tech.csm.dao;

import java.util.List;

import tech.csm.domain.AdmissionDtls;

public interface AdmissionDtlsDao {

	String saveAdmissionDtls(AdmissionDtls admissionDtls);

	List<AdmissionDtls> getAllAdmissionDtls();

	String cancelAdmission(int id);

}
