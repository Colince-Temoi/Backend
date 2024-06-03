package tech.csm.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import tech.csm.domain.AdmissionDtls;
import tech.csm.domain.Collage;

@Repository
public class AdmissionDtlsDaoImpl implements AdmissionDtlsDao {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcCall simpleJdbcCall;
	
	 @Autowired
	    private JdbcTemplate jdbcTemplate;
	 
	 public List<Map<String, Object>> fetchAllAdmissionDetails() {
	        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource)
	                .withProcedureName("p_AdmissionDtl_screen")
	                .declareParameters(
	                        new SqlParameter("p_status", Types.VARCHAR),
	                        new SqlParameter("p_AplicantName", Types.VARCHAR),
	                        new SqlParameter("p_EnrollmentId", Types.INTEGER),
	                        new SqlParameter("p_FourthOptional", Types.VARCHAR),
	                        new SqlParameter("p_EnrollmentDate", Types.DATE),
	                        new SqlParameter("p_CollageId", Types.INTEGER),
	                        new SqlOutParameter("o_msg", Types.VARCHAR)
	                ).returningResultSet("admissionDtls", new RowMapper<Map<String, Object>>() {
	                	@Override
	                    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
	                        Map<String, Object> result = new HashMap<>();
	                        result.put("enrollmentId", rs.getInt("enrollment_id"));
	                        result.put("applicanyName", rs.getString("applicany_name"));
	                        result.put("fourthOptional", rs.getString("fourth_optional"));
	                        result.put("enrollmentDate", rs.getDate("enrollment_date"));
	                        result.put("collageId", rs.getInt("collage_id"));
	                        return result;
	                    }
	                });

	        MapSqlParameterSource paramMap = new MapSqlParameterSource();
	        paramMap.addValue("p_status", "selectAllAdmissionDetails");
	        paramMap.addValue("p_AplicantName", null);
	        paramMap.addValue("p_EnrollmentId", 0);
	        paramMap.addValue("p_FourthOptional", null);
	        paramMap.addValue("p_EnrollmentDate", null);
	        paramMap.addValue("p_CollageId", 0);

	        Map<String, Object> resultMap = simpleJdbcCall.execute(paramMap);
	        List<Map<String, Object>> admissionDtls = (List<Map<String, Object>>) resultMap.get("admissionDtls");
	        return admissionDtls;
	    }

//	@PostConstruct // Initializes 'simpleJdbcCall' after dependency injection
//    public void init() {
//        this.simpleJdbcCall = new SimpleJdbcCall(dataSource)
//                .withProcedureName("p_AdmissionDtl_screen");
//    }
//
//    @Override
//    public String saveAdmissionDtls(AdmissionDtls admissionDtls) {
//		// Set enrolment_date to the current system date
//		LocalDate enrolment_date = LocalDate.now();
//
//		admissionDtls.setEnrollmentDate(enrolment_date);
//        SqlParameterSource inParams = new MapSqlParameterSource()
//                .addValue("p_status", "insAdmission")
//                .addValue("p_AplicantName", admissionDtls.getApplicanyName())
//                .addValue("p_EnrollmentId", admissionDtls.getEnrollmentId())  
//                .addValue("p_FourthOptional", admissionDtls.getFourthOptional())
//                .addValue("p_EnrollmentDate", admissionDtls.getEnrollmentDate()) 
//                .addValue("p_CollageId", admissionDtls.getCollage().getCollageId()); 
//
//       // Important: Declare output parameter for the stored procedure's message
//        simpleJdbcCall.declareParameters(new SqlOutParameter("o_msg", Types.VARCHAR));
//
//        Map<String, Object> out = simpleJdbcCall.execute(inParams);
//        return (String) out.get("o_msg");
//    }

//	@Override
//	public String saveAdmissionDtls(AdmissionDtls admissionDtls) {
//
//		// Set enrolment_date to the current system date
//		LocalDate enrolment_date = LocalDate.now();
//
//		admissionDtls.setEnrollmentDate(enrolment_date);
//
//		simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("p_AdmissionDtl_screen").declareParameters(
//				new SqlOutParameter("o_msg", Types.VARCHAR));
//
//		MapSqlParameterSource params = new MapSqlParameterSource().addValue("p_EnrollmentId", null, Types.INTEGER)
//				.addValue("p_AplicantName", admissionDtls.getApplicanyName())
//				.addValue("p_EnrollmentDate", admissionDtls.getEnrollmentDate()) // Assuming hireDate is java.sql.Date
//				.addValue("p_CollageId", admissionDtls.getCollage().getCollageId())
//				.addValue("p_FourthOptional", admissionDtls.getFourthOptional()).addValue("p_status", "insAdmission");
//
//		Map<String, Object> results = simpleJdbcCall.execute(params);
//
//		return (String) results.get("o_msg");
//	}

//	@Override
//	public List<AdmissionDtls> getAllAdmissionDtls() {
//		simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("p_AdmissionDtl_screen")
//				.returningResultSet("admnDtlResres", new RowMapper<AdmissionDtls>() {
//					
//					
//					@Override
//					public AdmissionDtls mapRow(ResultSet rs, int rowNum) throws SQLException {
//						AdmissionDtls admissionDtls = new AdmissionDtls();
//						admissionDtls.setApplicanyName(rs.getString(2));
//						admissionDtls.setEnrollmentId(rs.getInt(1));
//						admissionDtls.setEnrollmentDate(rs.getDate(4).toLocalDate());
//						admissionDtls.setFourthOptional(rs.getString(3));
//
//						Collage collage = new Collage();
//						collage.setCollageId(rs.getInt(5));
//
//						admissionDtls.setCollage(collage);
//
//						return admissionDtls;
//					}
//				});
//
//		MapSqlParameterSource msps = new MapSqlParameterSource()
//				.addValue("p_status", "selectAllAdmissionDetails", Types.VARCHAR)
//				.addValue("p_AplicantName", null, Types.VARCHAR).addValue("p_EnrollmentId", null, Types.INTEGER)
//				.addValue("p_CollageId", null, Types.INTEGER).addValue("p_FourthOptional", null, Types.VARCHAR)
//				.addValue("p_EnrollmentDate", null, Types.DATE);
//
//		Map<String, Object> res = simpleJdbcCall.execute(msps);
//
//		List<AdmissionDtls> admnDtlsList = (List<AdmissionDtls>) res.get("admnDtlResres");
//		return admnDtlsList;
//	}
	
	
//	@Override
//    public List<Map<String, Object>> fetchAllAdmissionDetails() {
//        // Initialize the SimpleJdbcCall object (do this only once, perhaps in a @PostConstruct method)
//        simpleJdbcCall = new SimpleJdbcCall(dataSource)
//                .withProcedureName("p_AdmissionDtl_screen")
//                .declareParameters(
//                        new SqlParameter("p_status", Types.VARCHAR),
//                        new SqlParameter("p_AplicantName", Types.VARCHAR),  
//                        new SqlParameter("p_EnrollmentId", Types.INTEGER),  
//                        new SqlParameter("p_FourthOptional", Types.VARCHAR),  
//                        new SqlParameter("p_EnrollmentDate", Types.DATE),
//                        new SqlParameter("p_CollageId", Types.INTEGER), 
//                        new SqlOutParameter("o_msg", Types.VARCHAR) // Out parameter
//                ).returningResultSet("admnDtlResres", new RowMapper<Map<String, Object>>() {
//                    @Override
//                    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
//                        Map<String, Object> result = new HashMap<>();
//                        result.put("enrollmentId", rs.getInt("enrollment_id"));
//                        result.put("applicanyName", rs.getString("applicany_name"));
//                        result.put("fourthOptional", rs.getString("fourth_optional"));
//                        result.put("enrollmentDate", rs.getDate("enrollment_date"));
//                        result.put("collageId", rs.getInt("collage_id"));
//                        return result;
//                    }
//                });
//
//        // Prepare input parameters
//        MapSqlParameterSource in = new MapSqlParameterSource()
//        		  .addValue("p_status", "selectAllAdmissionDetails")
//                  .addValue("p_AplicantName", null, Types.VARCHAR)  // Even if not used in the query, it's required
//                  .addValue("p_EnrollmentId", null, Types.INTEGER)  
//                  .addValue("p_FourthOptional", null, Types.VARCHAR)  
//                  .addValue("p_EnrollmentDate", null, Types.DATE) 
//                  .addValue("p_CollageId", null, Types.INTEGER);
//
//        // Execute the Stored Procedure
//        Map<String, Object> out = simpleJdbcCall.execute(in);
//
//        // Assuming your results are in the output parameter(s), 
//        // you'd extract them here and return as a List of Maps
//        return (List<Map<String, Object>>) out.get("admnDtlResres"); // Example, adjust based on your SP
//    }
	

	@Override
	public String cancelAdmission(int id) {
		simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("p_AdmissionDtl_screen").declareParameters(
				new SqlParameter("p_status", Types.VARCHAR), new SqlParameter("p_AplicantName", Types.VARCHAR),
				new SqlParameter("p_EnrollmentId", Types.INTEGER), new SqlParameter("p_EnrollmentDate", Types.DATE),
				new SqlParameter("p_CollageId", Types.INTEGER), new SqlParameter("p_FourthOptional", Types.VARCHAR),
				new SqlOutParameter("o_msg", Types.VARCHAR));

		MapSqlParameterSource msps = new MapSqlParameterSource().addValue("p_status", "cancelAdmission", Types.VARCHAR)
				.addValue("p_AplicantName", null, Types.VARCHAR).addValue("p_EnrollmentId", id, Types.INTEGER)
				.addValue("p_CollageId", null, Types.INTEGER).addValue("p_FourthOptional", null, Types.VARCHAR)
				.addValue("p_EnrollmentDate", null, Types.DATE);

		Map<String, Object> res = simpleJdbcCall.execute(msps);

		return (String) res.get("o_msg");
	}

	@Override
    public String saveAdmissionDtls(AdmissionDtls admissionDtls) {
        String status = "insAdmission";

		// Set enrolment_date to the current system date
		LocalDate enrolment_date = LocalDate.now();

		admissionDtls.setEnrollmentDate(enrolment_date);
        
        simpleJdbcCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("p_AdmissionDtl_screen")
                .declareParameters(
                        new SqlParameter("p_status", Types.VARCHAR),
                        new SqlParameter("p_AplicantName", Types.VARCHAR),
                        new SqlParameter("p_EnrollmentId", Types.INTEGER),  // Since enrollmentId is auto-incremented, pass Integer.MAX_VALUE
                        new SqlParameter("p_FourthOptional", Types.VARCHAR),
                        new SqlParameter("p_EnrollmentDate", Types.DATE),
                        new SqlParameter("p_CollageId", Types.INTEGER)
                );

        MapSqlParameterSource inParams = new MapSqlParameterSource();
        inParams.addValue("p_status", status);
        inParams.addValue("p_AplicantName", admissionDtls.getApplicanyName());
        inParams.addValue("p_FourthOptional", admissionDtls.getFourthOptional());
        inParams.addValue("p_EnrollmentDate", admissionDtls.getEnrollmentDate());
        inParams.addValue("p_CollageId", admissionDtls.getCollage().getCollageId());
        inParams.addValue("p_EnrollmentId", null);

        // Executing the stored procedure
        Map<String, Object> outValue = simpleJdbcCall.execute(inParams);

        // Getting the output parameter value
        String msg = (String) outValue.get("o_msg");

        return msg;
    }

	@Override
	public List<AdmissionDtls> getAllAdmissionDtls() {
		// TODO Auto-generated method stub
		return null;
	}

}
