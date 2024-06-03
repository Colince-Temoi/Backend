package tech.csm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import tech.csm.domain.AdmissionDtls;

public interface AdmissionDtlsDao {

	String saveAdmissionDtls(AdmissionDtls admissionDtls);

	List<AdmissionDtls> getAllAdmissionDtls();

	String cancelAdmission(int id);

	List<Map<String, Object>> fetchAllAdmissionDetails();


}
