package tech.csm.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.domain.Employee;

/*In a NutShell
 * If you need a List of records according to your domain, go for jdbcTemplate.query() flavors
 * If you need a single entry i.e by id, then utilize jdbcTemplate.queryForObject() flavors.
 * In this hands-on session we have discussed several alternatives that you can utilize.
 * */

@Controller
public class MainController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/getData")
	public String getRedgForm() {

//		1. jdbcTemplate.queryForObject() flavors

		// 1.1. Getting a single output value
		Double salary = jdbcTemplate.queryForObject("SELECT salary FROM hr.employees where employee_id='101'",
				Double.class);

//		System.out.println(salary);

		/*
		 * 1.2 Getting an Employee Object Useful when you need more fine-grained control
		 * over the data you are fetching.
		 * 
		 */

		Employee e = jdbcTemplate.queryForObject(
				"SELECT EMPLOYEE_ID,LAST_NAME,SALARY FROM hr.employees where employee_id='101'",
				new RowMapper<Employee>() {

					@Override
					public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
						// Create Employee object to hold the fetched Employee data
						Employee employee = new Employee();
						employee.setEmployeeId(rs.getInt(1));
						employee.setLastName(rs.getString(2));
						employee.setSalary(rs.getDouble(3));
						return employee;
					}
				});

		/*
		 * 1.3. Getting an Employee Object Less Verbose as Compared to the other flavor
		 * above Suitable for reading data that belongs to a single domain
		 */
		Employee e1 = jdbcTemplate.queryForObject(
				"SELECT EMPLOYEE_ID,LAST_NAME,SALARY FROM hr.employees where employee_id='101'",
				new BeanPropertyRowMapper<>(Employee.class));

//				  System.out.println(e); 
//				  System.out.println(e1);

//					2. jdbcTemplate.queryForObject() flavors

		/*
		 * 2.1. Reading a Map list
		 */
		List<Map<String, Object>> mapList = jdbcTemplate.queryForList("SELECT * FROM hr.employees");

		for (Map<String, Object> map : mapList) {
			// System.out.println(map);
			// Accessing data
		}
		/*
		 * List<Map<String, Object>> list = jdbcTemplate.queryForList(
		 * "SELECT EMPLOYEE_ID,LAST_NAME,SALARY,JOB_ID FROM hr.employees where JOB_ID=?"
		 * , new BeanPropertyRowMapper<>(Employee.class), "'IT_PROG'");
		 */
		SqlParameter parameter = new SqlParameter("IT_PROG", Types.VARCHAR);
//		System.out.println(parameter.getName());

		/*
		 * String sql = "SELECT FIRST_NAME FROM hr.employees where JOB_ID=?";
		 * List<String> employees = jdbcTemplate.queryForList(sql, String.class,
		 * "IT_PROG"); System.out.println(employees);
		 */

		List<Map<String, Object>> list1 = jdbcTemplate.queryForList(
				"SELECT EMPLOYEE_ID, LAST_NAME, SALARY, JOB_ID, HIRE_DATE FROM hr.employees where JOB_ID=?",
				new Object[] { "IT_PROG" }); // No more BeanPropertyRowMapper here

		for (Map<String, Object> map : list1) {
			System.out.println(map);
		}

		/*
		 * List<Employee> list2 = jdbcTemplate.queryForList(
		 * "SELECT EMPLOYEE_ID,LAST_NAME,SALARY,JOB_ID,HIRE_DATE FROM hr.employees where JOB_ID=?"
		 * , Employee.class, new Object[] {"IT_PROG"});
		 * 
		 */

		/*
		 * for (Employee employee : list2) { // System.out.println(employee; }
		 */

//		3. jdbcTemplate.query() flavors

//		3.1 --> Gives you a more fine grained control
		/*
		 * List<Employee> employeeList = jdbcTemplate.
		 * query("SELECT EMPLOYEE_ID,LAST_NAME,SALARY,JOB_ID,HIRE_DATE FROM hr.employees where JOB_ID='IT_PROG'"
		 * , new RowMapper<Employee>() {
		 * 
		 * @Override public Employee mapRow(ResultSet rs, int rowNum) throws
		 * SQLException { // Create Employee object to hold the fetched Employee data
		 * Employee employee = new Employee(); employee.setEId(rs.getInt(1));
		 * employee.setLastName(rs.getString(2)); employee.setSalary(rs.getDouble(3));
		 * employee.setJobId(rs.getString(4)); employee.setHireDate(rs.getDate(5));
		 * return employee; } });
		 */
		/*
		 * for (Employee employee : employeeList) { System.out.println(employee); }
		 */

//		3.2 -->Less Verbose
		/*
		 * List<Employee> empList = jdbcTemplate.query(
		 * "SELECT EMPLOYEE_ID,LAST_NAME,SALARY,JOB_ID,HIRE_DATE FROM hr.employees where JOB_ID=?"
		 * , new BeanPropertyRowMapper<>(Employee.class), "IT_PROG");
		 */
		/*
		 * for (Employee employee : empList) { System.out.println(employee); }
		 */
		return "test";
	}
}
