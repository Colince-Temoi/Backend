package tech.csm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import tech.csm.domain.AdmissionDtls;
import tech.csm.domain.Collage;

@Repository
public class AdmissionDtlsDaoImpl implements AdmissionDtlsDao {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcCall simpleJdbcCall;

	@Override
	public String saveAdmissionDtls(AdmissionDtls admissionDtls) {

		// Set enrolment_date to the current system date
		LocalDate enrolment_date = LocalDate.now();

		admissionDtls.setEnrollmentDate(enrolment_date);

		simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("p_AdmissionDtl_screen").declareParameters(
				new SqlParameter("p_status", Types.VARCHAR), new SqlParameter("p_AplicantName", Types.VARCHAR),
				new SqlParameter("p_EnrollmentId", Types.INTEGER), new SqlParameter("p_EnrollmentDate", Types.DATE),
				new SqlParameter("p_CollageId", Types.INTEGER), new SqlParameter("p_FourthOptional", Types.VARCHAR),
				new SqlOutParameter("o_msg", Types.VARCHAR));

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("p_EnrollmentId", null, Types.INTEGER)
				.addValue("p_AplicantName", admissionDtls.getApplicanyName())
				.addValue("p_EnrollmentDate", admissionDtls.getEnrollmentDate()) // Assuming hireDate is java.sql.Date
				.addValue("p_CollageId", admissionDtls.getCollage().getCollageId())
				.addValue("p_FourthOptional", admissionDtls.getFourthOptional()).addValue("p_status", "insAdmission");

		Map<String, Object> results = simpleJdbcCall.execute(params);

		return (String) results.get("o_msg");
	}

	@Override
	public List<AdmissionDtls> getAllAdmissionDtls() {
		simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("p_AdmissionDtl_screen")
				.returningResultSet("admnDtlResres", new RowMapper<AdmissionDtls>() {
					@Override
					public AdmissionDtls mapRow(ResultSet rs, int rowNum) throws SQLException {
						AdmissionDtls admissionDtls = new AdmissionDtls();
						admissionDtls.setApplicanyName(rs.getString(2));
						admissionDtls.setEnrollmentId(rs.getInt(1));
						admissionDtls.setEnrollmentDate(rs.getDate(4).toLocalDate());
						admissionDtls.setFourthOptional(rs.getString(3));

						Collage collage = new Collage();
						collage.setCollageId(rs.getInt(5));

						admissionDtls.setCollage(collage);

						return admissionDtls;
					}
				});

		MapSqlParameterSource msps = new MapSqlParameterSource()
				.addValue("p_status", "selectAllAdmissionDetails", Types.VARCHAR)
				.addValue("p_AplicantName", null, Types.VARCHAR).addValue("p_EnrollmentId", null, Types.INTEGER)
				.addValue("p_CollageId", null, Types.INTEGER).addValue("p_FourthOptional", null, Types.VARCHAR)
				.addValue("p_EnrollmentDate", null, Types.DATE);

		Map<String, Object> res = simpleJdbcCall.execute(msps);

		List<AdmissionDtls> admnDtlsList = (List<AdmissionDtls>) res.get("admnDtlResres");
		return admnDtlsList;
	}

	@Override
	public String cancelAdmission(int id) {
	    simpleJdbcCall = new SimpleJdbcCall(dataSource)
	            .withProcedureName("p_AdmissionDtl_screen")
	            .declareParameters(
	    				new SqlParameter("p_status", Types.VARCHAR), new SqlParameter("p_AplicantName", Types.VARCHAR),
	    				new SqlParameter("p_EnrollmentId", Types.INTEGER), new SqlParameter("p_EnrollmentDate", Types.DATE),
	    				new SqlParameter("p_CollageId", Types.INTEGER), new SqlParameter("p_FourthOptional", Types.VARCHAR),
	    				new SqlOutParameter("o_msg", Types.VARCHAR));

	    MapSqlParameterSource msps = new MapSqlParameterSource()
				.addValue("p_status", "cancelAdmission", Types.VARCHAR)
				.addValue("p_AplicantName", null, Types.VARCHAR).addValue("p_EnrollmentId", id, Types.INTEGER)
				.addValue("p_CollageId", null, Types.INTEGER).addValue("p_FourthOptional", null, Types.VARCHAR)
				.addValue("p_EnrollmentDate", null, Types.DATE);
	    
	    Map<String, Object> res = simpleJdbcCall.execute(msps);

	    return (String) res.get("o_msg");
	}


}
