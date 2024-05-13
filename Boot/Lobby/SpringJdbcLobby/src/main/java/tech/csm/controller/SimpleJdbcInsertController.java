package tech.csm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.domain.Employee2;

/*
 * Why Batch Processing with SimpleJdbcInsert

Simplified Syntax: Reduces boilerplate code for preparing individual insert statements.
Performance: Batching inserts often offers performance advantages over executing multiple individual inserts.
Metadata Handling: SimpleJdbcInsert handles database metadata introspection.

Efficiency: SimpleJdbcInsert  analyzes your table's metadata to construct the insert statement.  It handles this setup cost once and then efficiently reuses the prepared statement for multiple inserts.
Convenience: It allows you to provide data as SqlParameterSource objects (often using maps), simplifying parameter handling for batch operations.
 * */

@Controller
public class SimpleJdbcInsertController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/SimpleJdbInserttest2")
	public String getRedgForm() {
		
        List<Employee2> employees = new ArrayList<>();
        for (int i = 5; i < 8; i++) {
            employees.add(new Employee2(null, // Employee ID will be auto-generated 
                                        "FirstName" + i,
                                        "email" + i + "@example.com",
                                        "JOB_" + i,
                                        Calendar.getInstance().getTime(),
                                        5000.0 + (i * 1000)));
        }
		
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("emp1")
                .usingGeneratedKeyColumns("employee_id"); // Assuming auto-generated ID
		

List<SqlParameterSource> parameters = new ArrayList<>();
for (Employee2 employee : employees) {
parameters.add(new MapSqlParameterSource()
.addValue("first_name", employee.getFirstName())
.addValue("email", employee.getEmail())
.addValue("job_id", employee.getJobId())
.addValue("hire_date", employee.getHireDate())
.addValue("salary", employee.getSalary()));
}

int[] updateCounts = insert.executeBatch(parameters.toArray(new SqlParameterSource[0]));

for (int i : updateCounts) {
	System.out.println(i);
}

//		SimpleJdbcInsert sjdbci = new SimpleJdbcInsert(jdbcTemplate.getDataSource());
//
//		Employee2 employee = new Employee2();
//		employee.setFirstName("Jack");
//		employee.setEmail("tenyi@example.com");
//		employee.setSalary(30000.0);
//		employee.setJobId("IT_PROG");
//		try {
//			employee.setHireDate(new SimpleDateFormat("dd-MM-yyy").parse("2019-08-21"));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// Create a MapSqlParameterSource object
//		    MapSqlParameterSource parameters = new MapSqlParameterSource()
//		        .addValue("firstName", employee.getFirstName())
//		        .addValue("email", employee.getEmail())
//		        .addValue("jobId", employee.getJobId())
//		        .addValue("hireDate", employee.getHireDate())
//		        .addValue("salary", employee.getSalary());

//		sjdbci.withTableName("emp1").usingGeneratedKeyColumns("emp_id");

//			    Number generatedKey = sjdbci.executeAndReturnKey(parameters);

//		Number generatedKey = sjdbci.executeAndReturnKey(new BeanPropertySqlParameterSource(employee));
//
//		System.out.println("Generated Key: " + generatedKey);

		return "test4";
	}
}
/*
 * This is a bit easier as: 1. I don't need to write the query for the insert
 * statement. 2. Table name I have given: check the
 * behavior-withTableName(table_name) 3. PK name I have given: check the
 * behavior-usingGeneratedKeyColumns(PK) 4. Column names and their respective
 * values I have given: check-I have passed through MapSqlParameterSource
 * 
 * Finally am executing the things and everything is working in a seamless way.
 * If you have domain objects with data, no need to use MapSqlParameterSource.
 * Use one overloaded method like: Number gk = sjdbi.executeAndReturnKey(new
 * BeanPropertySqlParameterSource(employee));
 */