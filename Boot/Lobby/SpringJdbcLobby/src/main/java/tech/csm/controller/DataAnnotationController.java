package tech.csm.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.javafaker.Faker;

import tech.csm.domain.Employee;
import tech.csm.domain.Employee2;
import tech.csm.domain.Job;
import tech.csm.domain.JobHistory;

@RestController
//@Controller
public class DataAnnotationController {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
//	 @PostMapping("/postData")
//	    public ResponseEntity<List<Map<String, Object>>> createPost() {
//		 
//		 
//
//	        String sql = "INSERT INTO emp1 (first_name, email, job_id, hire_date, salary) VALUES (?, ?, ?, ?, ?)";
//	       
//	        
//	        List<Employee2> employees = new ArrayList<>();
//	        for (int i = 0; i < 5; i++) {
//	            employees.add(new Employee2(null, // Employee ID will be auto-generated 
//	                                        "FirstName" + i,
//	                                        "email" + i + "@example.com",
//	                                        "JOB_" + i,
//	                                        Calendar.getInstance().getTime(),
//	                                        5000.0 + (i * 1000)));
//	        }
//	        
//	        
//	        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
//			
//	        
//
//	        int[] updateCounts = jdbcTemplate.batchUpdate(new PreparedStatementCreator() {
//
//				@Override
//				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//					return ps;
//				}
//			}, new BatchPreparedStatementSetter() {
//
//				@Override
//				public void setValues(PreparedStatement ps, int i) throws SQLException {
//					ps.setString(1, employees.get(i).getFirstName());
//					ps.setString(2, employees.get(i).getEmail());
//					ps.setString(3, employees.get(i).getJobId());
//					ps.setDate(4, new java.sql.Date(employees.get(i).getHireDate().getTime()));
//					ps.setDouble(5, employees.get(i).getSalary());
//
//				}
//
//				@Override
//				public int getBatchSize() {
//					return employees.size();
//				}
//			}, generatedKeyHolder);
//
//			// Print the keys
//			for (int i = 0; i < updateCounts.length; i++) {
//				System.out.println("Generated key for employee " + i + ": " + generatedKeyHolder.getKeyList().get(i));
//			}
//	        return ResponseEntity.ok(generatedKeyHolder.getKeyList()); 
//	    }
	
//	@PostMapping("/postData")
//    public ResponseEntity<String> createPost() { 
//        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
//
//        String sql = "INSERT INTO hr.jobs1 (JOB_ID, JOB_TITLE, MIN_SALARY, MAX_SALARY) VALUES (:jobId, :jobTitle, :minSalary, :maxSalary)";
//
//        List<Job> jobs = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            jobs.add(new Job("JOB_" + i, "Job Title " + i, 1000 * (i + 1), 2000 * (i + 1)));
//        }
//        
//        SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(jobs.toArray()); 
//        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchArgs); 
//
//        return ResponseEntity.ok(String.format("Inserted %d rows successfully", Arrays.stream(updateCounts).sum())); 
        
//        String sql = "INSERT INTO hr.jobs1 (JOB_ID, JOB_TITLE, MIN_SALARY, MAX_SALARY) " +
//                     "VALUES (:jobId, :jobTitle, :minSalary, :maxSalary)";
//        
//     // Generating Sample Data
//        List<Job> jobs = new ArrayList<>();
//        for (int i = 1; i <= 10; i++) {
//            jobs.add(new Job("JOB_" + i, "Job Title " + i, 10000 * i, 20000 * i));
//        }
//
//        SqlParameterSource[] batchArgs = jobs.stream()
//                .map(job -> new MapSqlParameterSource()
//                        .addValue("jobId", job.getJobId())
//                        .addValue("jobTitle", job.getJobTitle())
//                        .addValue("minSalary", job.getMinSalary())
//                        .addValue("maxSalary", job.getMaxSalary()))
//                .toArray(SqlParameterSource[]::new);
//
//        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
//        
//        for (int i : updateCounts) {
//			System.out.println(i);
//		}
//
//        return ResponseEntity.ok("Post created successfully");
//    }
	
	
	@GetMapping("/getDataAnn")
	public String getEmployeeData() {
		
		 NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());

	        // Using Faker to generate dummy data
	        Faker faker = new Faker();

	        JobHistory jobHistory = new JobHistory();
	        jobHistory.setStartDate( faker.date().past(3650, java.util.concurrent.TimeUnit.DAYS));// Past Start Date
	        jobHistory.setEndDate(faker.date().future(3650, java.util.concurrent.TimeUnit.DAYS));  // Future End Date
	        jobHistory.setJobId(faker.job().title());
	        jobHistory.setDepartmentId(faker.number().numberBetween(10, 50)); // Example ID range

	        String sql = "INSERT INTO hr.job_history1 (employee_id, start_date, end_date, job_id, department_id) " +
	                     "VALUES (:employeeId, :startDate, :endDate, :jobId, :departmentId)";

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	                .addValue("employeeId", jobHistory.getEmployeeId())
	                .addValue("startDate", jobHistory.getStartDate(),java.sql.Types.DATE)
	                .addValue("endDate", jobHistory.getEndDate(),java.sql.Types.DATE)
	                .addValue("jobId", jobHistory.getJobId())
	                .addValue("departmentId", jobHistory.getDepartmentId());

	        int rowsInserted = namedParameterJdbcTemplate.update(sql, paramSource);

	        // Assuming some sort of logging
	        System.out.println("Rows inserted: " + rowsInserted);

	
        
        
        
        
        
        
        
        
        
          
        
        
		Employee emp1 = namedParameterJdbcTemplate.queryForObject(
				"SELECT EMPLOYEE_ID, LAST_NAME, SALARY, JOB_ID, HIRE_DATE FROM employees WHERE EMPLOYEE_ID = :employeeId",
				new MapSqlParameterSource().addValue("employeeId", 102), new BeanPropertyRowMapper<>(Employee.class)); 
//		System.out.println(emp1);
	            return "dataAnn";
	                    }
	
	@GetMapping("/deleteRecord") // Assuming a different endpoint
    public String deleteRecord() {

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());

        String sql = "DELETE FROM hr.job_history1 WHERE id = 8";

        SqlParameterSource paramSource = new MapSqlParameterSource()
               .addValue("id", 8); // ID to be deleted

        int rowsDeleted = namedParameterJdbcTemplate.update(sql, paramSource);

        System.out.println("Rows deleted: " + rowsDeleted);

        return "recordDeleted"; // Enhance the response as needed
    }
	
	
	@DeleteMapping("/deleteRecord/{id}")
	 public ResponseEntity<Void> deleteRecord(@PathVariable("id") Integer employeeId) {

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());

        String sql = "DELETE FROM hr.job_history1 WHERE employee_id = :employeeId";

        SqlParameterSource paramSource = new MapSqlParameterSource()
               .addValue("employeeId", employeeId); // ID to be deleted

        int rowsDeleted = namedParameterJdbcTemplate.update(sql, paramSource);

        System.out.println("Rows deleted: " + rowsDeleted);
		return null;

//        return "recordDeleted"; // Enhance the response as needed
    }
	
	
	 @GetMapping("/updateRecord") // Assuming a different endpoint
	    public String updateRecord() {

	        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());

	        // Using Faker to generate updated data
	        Faker faker = new Faker();

	        JobHistory jobHistory = new JobHistory();
	        // Do not change the ID
	        jobHistory.setStartDate( faker.date().past(3650, java.util.concurrent.TimeUnit.DAYS));    
	        jobHistory.setEndDate(faker.date().future(3650, java.util.concurrent.TimeUnit.DAYS));  
	        jobHistory.setJobId(faker.job().title());
	        jobHistory.setDepartmentId(faker.number().numberBetween(10, 50)); 

	        String sql = "UPDATE hr.job_history1 SET " +
	                     "start_date = :startDate, end_date = :endDate, " +
	                     "job_id = :jobId, department_id = :departmentId " +
	                     "WHERE employee_id = 1"; // Update where id = 1

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	               .addValue("startDate", jobHistory.getStartDate(), java.sql.Types.DATE)
	               .addValue("endDate", jobHistory.getEndDate(), java.sql.Types.DATE)
	               .addValue("jobId", jobHistory.getJobId())
	               .addValue("departmentId", jobHistory.getDepartmentId());

	        int rowsUpdated = namedParameterJdbcTemplate.update(sql, paramSource);

	        System.out.println("Rows updated: " + rowsUpdated);

	        return "test3"; // Enhance the response as needed
	    }
		
		
//		    final String p_status = "UpdateEmployeeById";
//	        final String p_firstName = "UpdatedFirstName";
//	        final String p_jobId = "UpdJobId";
//	        final String p_email = "updated@example.com";
//	        final Date p_hireDate = new Date(System.currentTimeMillis()); // Set the appropriate hire date
//	        final double p_salary = 5678.90;
//
//	        // Create a CallableStatementCreator to provide the SQL and parameters
//	        CallableStatementCreator callableStatementCreator = new CallableStatementCreator() {
//	            @Override
//	            public CallableStatement createCallableStatement(Connection con) throws SQLException {
//	                String sql = "{call p_employee_screen(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
//	                CallableStatement cs = con.prepareCall(sql);
//	                cs.setString(1, p_status);
//	                cs.setInt(3, 4);
//	                cs.setString(2, p_firstName);
//	                cs.setString(4, p_jobId);
//	                cs.setString(5, p_email);
//	                cs.setDate(6, new java.sql.Date(p_hireDate.getTime()));
//	                cs.setDouble(7, p_salary);
//
//	                // Set null for the OUT parameters since they are not used for update operation
//	                cs.registerOutParameter(8, Types.DOUBLE);
//	                cs.registerOutParameter(9, Types.VARCHAR);
//
//	                return cs;
//	            }
//	        };
//
//	        // Define the CallableStatementCallback implementation to handle the stored procedure call
//	        CallableStatementCallback<String> callableStatementCallback = new CallableStatementCallback<String>() {
//	            @Override
//	            public String doInCallableStatement(CallableStatement cs) throws SQLException {
//	                cs.execute();
//
//	                // Retrieve the output message from o_msg (not needed for update operation)
//	                String outputMessage = cs.getString(9);
//	                return outputMessage;
//	            }
//	        };
//
//	        // Execute the stored procedure and get the output message
//	        String outputMessage = jdbcTemplate.execute(callableStatementCreator, callableStatementCallback);
//
//	        // Print the output message (not needed for update operation)
//	        System.out.println("Output message: " + outputMessage);
        
//		return "dataAnn";
	}
