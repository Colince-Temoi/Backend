package tech.csm.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.github.javafaker.Faker; // Assuming you have the Faker library for generating data

import tech.csm.domain.Department;
import tech.csm.domain.Employee;

@Controller
public class NpJdbcTemplate {

	@Autowired

	private JdbcTemplate jdbcTemplate;

	@GetMapping("/NamedParameterJdbcTemplate")
	public String getRedgForm() {

		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
//		System.out.println(npjt);

//		Alternative 1 : Reading a Single Employee related data.

		Map<String, Object> pMap = new HashMap<>();
		pMap.put("empId", 101);

		Employee emp = npjt.queryForObject(
				"SELECT EMPLOYEE_ID,LAST_NAME,SALARY,JOB_ID,HIRE_DATE FROM hr.employees where EMPLOYEE_ID=:empId", pMap,
				new BeanPropertyRowMapper<>(Employee.class));

//		System.out.println(emp);

//		Alternative 2: Reading a Single Employee related data.-> Binding some value(s) to the named parameter instead of using this plain Map object
		Employee emp1 = npjt.queryForObject(
				"SELECT EMPLOYEE_ID,LAST_NAME,SALARY,JOB_ID,HIRE_DATE FROM hr.employees where EMPLOYEE_ID=:empId",
				new MapSqlParameterSource().addValue("empId", 102), new BeanPropertyRowMapper<>(Employee.class));

		// Code for reading data (Alternative 2 with SQL types)
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("empId", 102, java.sql.Types.INTEGER); // Specify SQL type for empId

		Employee emp2 = npjt.queryForObject(
				"SELECT EMPLOYEE_ID, LAST_NAME, SALARY, JOB_ID, HIRE_DATE FROM hr.employees where EMPLOYEE_ID=:empId",
				params, new BeanPropertyRowMapper<>(Employee.class));

//		System.out.println(emp2);

//		Alternative 1 : Reading a List of Employees related data.
		List<Employee> empList = npjt.query(
				"Select e.EMPLOYEE_ID,e.LAST_NAME,e.SALARY,e.JOB_ID,e.HIRE_DATE,e.DEPARTMENT_ID,d.DEPARTMENT_NAME\r\n"
						+ "from employees e\r\n" + "inner join departments d\r\n" + "using(DEPARTMENT_ID)",
				new RowMapper<Employee>() {

					Employee employee = null;
					Department department = null;

					@Override
					public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
						employee = new Employee();

						employee.setEmployeeId(rs.getInt(1));
						employee.setLastName(rs.getString(2));
						employee.setSalary(rs.getDouble(3));
						employee.setJobId(rs.getString(4));
						employee.setHireDate(rs.getDate(5));

						department = new Department();
						department.setDeptId(rs.getInt(6));
						department.setDeptName(rs.getString(7));

						employee.setDepartment(department);

						return employee;
					}

				});

//		empList.forEach((x)->System.out.println(x));

//		Now lets read from Employees table where job_id is IT_PROG
		List<Employee> empList1 = npjt.query(
				"SELECT EMPLOYEE_ID,LAST_NAME,SALARY,JOB_ID FROM hr.employees where JOB_ID=:jobId",
				new MapSqlParameterSource().addValue("jobId", "IT_PROG"), new RowMapper<Employee>() {

					Employee employee = null;

					@Override
					public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
						employee = new Employee();

						employee.setEmployeeId(rs.getInt(1));
						employee.setLastName(rs.getString(2));
						employee.setSalary(rs.getDouble(3));
						employee.setJobId(rs.getString(4));

						return employee;
					}

				});

//		empList1.forEach((x) -> System.out.println(x));

		// Code for inserting data into emp1 table
		Faker faker = new Faker();
//		KeyHolder keyHolder = new GeneratedKeyHolder(); // To store the generated ID
//
//		SqlParameterSource parameters = new MapSqlParameterSource()
//				.addValue("first_name", faker.name().firstName(), java.sql.Types.VARCHAR)
//				.addValue("email", faker.internet().emailAddress(), java.sql.Types.VARCHAR)
//				.addValue("job_id", "IT_PROG", java.sql.Types.VARCHAR)
//				.addValue("hire_date", faker.date().past(3650, java.util.concurrent.TimeUnit.DAYS), java.sql.Types.DATE)
//				.addValue("salary", faker.number().randomDouble(2, 5000, 15000), java.sql.Types.DOUBLE);
//
//		String insertSql = "INSERT INTO emp1 (first_name, email, job_id, hire_date, salary) "
//				+ "VALUES (:first_name, :email, :job_id, :hire_date, :salary)";
//
//		npjt.update(insertSql, parameters, keyHolder);
//
//		int generatedId = keyHolder.getKey().intValue(); // Retrieve the generated ID

		// Success message using the ID
//		System.out.println("New employee record created with ID: " + generatedId);

		// Code for update
		String updateSql = "UPDATE emp1 SET " + "first_name = :first_name, " + "email = :email, " + "job_id = :job_id, "
				+ "salary = :salary " + "WHERE emp_id = :emp_id";

		SqlParameterSource parameters1 = new MapSqlParameterSource()
				.addValue("first_name", faker.name().firstName(), java.sql.Types.VARCHAR)
				.addValue("email", faker.internet().emailAddress(), java.sql.Types.VARCHAR)
				.addValue("job_id", "AD_ASST", java.sql.Types.VARCHAR) // Update with a new job
				.addValue("salary", faker.number().randomDouble(2, 8000, 12000), java.sql.Types.DOUBLE)
				.addValue("emp_id", 7, java.sql.Types.INTEGER);

		int rowsUpdated = npjt.update(updateSql, parameters1);

		if (rowsUpdated > 0) {
			System.out.println("Employee with ID 7 updated successfully!");
		} else {
			System.out.println("Employee with ID 7 not found.");
		}

		return "test3";

	}
}