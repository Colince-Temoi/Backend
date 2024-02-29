package tech.csm.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.domain.Employee2;

@Controller
public class BatchProcessingController2 {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/batchinserttest2")
	public String getRedgForm() {

//		1. batch insert using JdbcTemplate

		// Create a list of employees
		List<Employee2> employees = new ArrayList<>();

		Employee2 e1 = new Employee2();
		e1.setFirstName("Praj");
		e1.setEmail("praj@example.com");
		e1.setSalary(40000.0);
		e1.setJobId("PM");
		try {
			e1.setHireDate(new SimpleDateFormat("dd-MM-yyy").parse("2012-01-01"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Employee2 e2 = new Employee2();
		e2.setFirstName("Supri");
		e2.setEmail("supri@example.com");
		e2.setSalary(30000.0);
		e2.setJobId("IT");
		try {
			e2.setHireDate(new SimpleDateFormat("dd-MM-yyy").parse("2017-02-01"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Employee2 e3 = new Employee2();
		e3.setFirstName("Alice");
		e3.setEmail("alice@example.com");
		e3.setSalary(70000.0);
		e3.setJobId("SA");
		try {
			e3.setHireDate(new Date(new SimpleDateFormat("dd-MM-yyy").parse("2018-03-01").getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		employees.add(e1);
		employees.add(e2);
		employees.add(e3);

		String sql = "INSERT INTO emp1 (first_name, email, job_id, hire_date, salary) VALUES (?, ?, ?, ?, ?)";

		KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

		int[] updateCounts = jdbcTemplate.batchUpdate(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				return ps;
			}
		}, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, employees.get(i).getFirstName());
				ps.setString(2, employees.get(i).getEmail());
				ps.setString(3, employees.get(i).getJobId());
				ps.setDate(4, new java.sql.Date(employees.get(i).getHireDate().getTime()));
				ps.setDouble(5, employees.get(i).getSalary());

			}

			@Override
			public int getBatchSize() {
				return employees.size();
			}
		}, generatedKeyHolder);

		// Print the keys
		for (int i = 0; i < updateCounts.length; i++) {
			System.out.println("Generated key for employee " + i + ": " + generatedKeyHolder.getKeyList().get(i));
		}

		return "test4";
	}
}

/*
 * Explanation of what is happenning here For the second parameter:
 * PreparedStatementSetter anonymous inner class implementation. It has two
 * callback functions. What will happen then? The callback function getBatchSize
 * returns the size of our AL store implying giving an intimation to the
 * setValues callback function how many times it should iterate. During the
 * first iteration i will hold 1 and so on, indicating the an Employee object at
 * the particular index in the AL. ] In a nutshell, how many times the first
 * callback function will be called, that depends upon the size in the second
 * call back function. *
 */
