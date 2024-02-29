package tech.csm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.domain.Employee2;

@Controller
public class SimpleJdbcInsertController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/SimpleJdbInserttest2")
	public String getRedgForm() {

		SimpleJdbcInsert sjdbci = new SimpleJdbcInsert(jdbcTemplate.getDataSource());

		Employee2 employee = new Employee2();
		employee.setFirstName("Jack");
		employee.setEmail("tenyi@example.com");
		employee.setSalary(30000.0);
		employee.setJobId("IT_PROG");
		try {
			employee.setHireDate(new SimpleDateFormat("dd-MM-yyy").parse("2019-08-21"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create a MapSqlParameterSource object
//		    MapSqlParameterSource parameters = new MapSqlParameterSource()
//		        .addValue("firstName", employee.getFirstName())
//		        .addValue("email", employee.getEmail())
//		        .addValue("jobId", employee.getJobId())
//		        .addValue("hireDate", employee.getHireDate())
//		        .addValue("salary", employee.getSalary());

		sjdbci.withTableName("emp1").usingGeneratedKeyColumns("emp_id");

//			    Number generatedKey = sjdbci.executeAndReturnKey(parameters);

		Number generatedKey = sjdbci.executeAndReturnKey(new BeanPropertySqlParameterSource(employee));

		System.out.println("Generated Key: " + generatedKey);

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