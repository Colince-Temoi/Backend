package tech.csm.service;

import java.sql.Date;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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

	@Override
	public List<Map<String, Object>> fetchAllAdmissionDetails() {
		List<Map<String, Object>> admissionDtlMap = admissionDtlsDao.fetchAllAdmissionDetails();

//		ModelMapper modelMapper = new ModelMapper();
//		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//		
//		List<AdmissionDtls> admissionDtlsList = admissionDtlMap.stream()
//			    .map(map -> modelMapper.map(map, AdmissionDtls.class))
//			    .collect(Collectors.toList());
//		for (AdmissionDtls admissionDtls : admissionDtlsList) {
//			System.out.println(admissionDtls);
//		}

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		

		List<AdmissionDtls> admissionDtlsList = admissionDtlMap.stream().map(map -> {
			AdmissionDtls admissionDtls = modelMapper.map(map, AdmissionDtls.class);
			
//			System.out.println(map.get("enrollmentDate"));

			admissionDtls.setEnrollmentDate(((Date) map.get("enrollmentDate")).toLocalDate());

			// Fetch Collage (assuming you have a CollageRepository)
			Collage collage = collageDao.getCollageById((Integer) map.get("collageId"));

			admissionDtls.setCollage(collage);
			return admissionDtls;
		}).collect(Collectors.toList());

		for (AdmissionDtls x : admissionDtlsList) {
			System.out.println(x);
		}

		return admissionDtlMap;
	}

}
