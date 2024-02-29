package tech.csm.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.domain.Employee2;

@Controller
public class BatchProcessingController {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/batchinserttest")
	public String getRedgForm() {

//		1. batch insert using NamedParameterJdbcTemplate
		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());

		// Create a list of employees
		List<Employee2> employees = new ArrayList<>();

		Employee2 e1 = new Employee2();
		e1.setFirstName("John");
		e1.setEmail("john@example.com");
		e1.setSalary(50000.0);
		e1.setJobId("DEV");
		try {
			e1.setHireDate(new SimpleDateFormat("dd-MM-yyy").parse("2022-01-01"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Employee2 e2 = new Employee2();
		e2.setFirstName("Jane");
		e2.setEmail("jane@example.com");
		e2.setSalary(60000.0);
		e2.setJobId("QA");
		try {
			e2.setHireDate(new SimpleDateFormat("dd-MM-yyy").parse("2022-02-01"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Employee2 e3 = new Employee2();
		e3.setFirstName("Bob");
		e3.setEmail("bob@example.com");
		e3.setSalary(70000.0);
		e3.setJobId("PM");
		try {
			e3.setHireDate(new Date(new SimpleDateFormat("dd-MM-yyy").parse("2022-03-01").getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		employees.add(e1);
		employees.add(e2);
		employees.add(e3);

		// Create a SqlParameterSource array
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(employees.toArray());

//		System.out.println(Arrays.toString(batchArgs));

		// Create a KeyHolder
		KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

		// Execute the batch update
		int[] updateCounts = npjt.batchUpdate(
				"INSERT INTO emp1 (first_name, email, job_id, hire_date, salary) VALUES (:firstName, :email, :jobId, :hireDate, :salary)",
				batchArgs, generatedKeyHolder);

		// Print the keys
		for (int i = 0; i < updateCounts.length; i++) {
			System.out.println("Generated key for employee " + i + ": " + generatedKeyHolder.getKeyList().get(i));
		}
		
		
//		Alternative 2
		

		return "test4";
	}
}
