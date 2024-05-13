package tech.csm.controller;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.domain.Employee2;

/*
 Explanation of Changes


RowMapper<Map<String, Object>>: Instead of using BeanPropertyRowMapper with the Employee2 class, we've introduced a custom RowMapper that maps each row in the ResultSet to a Map<String, Object>. This makes it generic and not tied to any specific domain class.

ResultSetMetaData: Inside the RowMapper, ResultSetMetaData is used to dynamically fetch column names and values from the ResultSet. This allows for a flexible mapping without needing a predefined domain class. 

Printing the Output: The final output loop iterates over each map in the list (each map representing an employee record), and then iterates over each key-value pair within that map to print the employee data in a human readable format.


Advantages
This approach provides more flexibility as you are not bound by a domain class and can handle changes in the stored procedure result set without code modifications (provided that the data types returned are compatible with the Object class).
 * */

@Controller
public class SimpleJdbcCall1 {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/SimpleJdbcCall")
	public String getRedgForm() {
		

		SimpleJdbcCall sjc = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName("p_employee_screen")
				.declareParameters(new SqlParameter("p_status", Types.VARCHAR),
						new SqlParameter("p_firstName", Types.VARCHAR), new SqlParameter("p_employeeId", Types.INTEGER),
						new SqlParameter("p_jobId", Types.VARCHAR), new SqlParameter("p_email", Types.VARCHAR),
						new SqlParameter("p_hireDate", Types.DATE), new SqlParameter("p_salary", Types.DOUBLE),
						new SqlOutParameter("o_sal", Types.DOUBLE), new SqlOutParameter("o_msg", Types.VARCHAR));

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("p_status", "getSalary")
				.addValue("p_firstName", null) // Providing null for non-required parameter
				.addValue("p_employeeId", 1).addValue("p_jobId", null).addValue("p_email", null)
				.addValue("p_hireDate", null).addValue("p_salary", null);

		Map<String, Object> result = sjc.execute(params);

		Double salary = (Double) result.get("o_sal");
		System.out.println("Salary for employee 1: " + salary);

//		Map<String, Object> res = new HashMap<String, Object>();
//
//		SimpleJdbcCall sjc = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName("p_employee_screen")
//				.returningResultSet("empres", new RowMapper<Map<String, Object>>() {
//					@Override
//					public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
//						ResultSetMetaData rsmd = rs.getMetaData();
//						Map<String, Object> map = new HashMap<>();
//						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//							map.put(rsmd.getColumnName(i), rs.getObject(i));
//						}
//						return map;
//					}
//				});
//
//		MapSqlParameterSource msps = new MapSqlParameterSource()
//				.addValue("p_status", "selectAllEmployees", Types.VARCHAR).addValue("p_firstName", null, Types.VARCHAR)
//				.addValue("p_employeeId", null, Types.INTEGER).addValue("p_jobId", null, Types.VARCHAR)
//				.addValue("p_email", null, Types.VARCHAR).addValue("p_hireDate", null, Types.DATE)
//				.addValue("p_salary", null, Types.DOUBLE);
//
//		res = sjc.execute(msps);
//
//		// No need to cast since it's already a List<Map<String, Object>>
//		List<Map<String, Object>> empList = (List<Map<String, Object>>) res.get("empres");
//
//		// Print each row in the list
//		for (Map<String, Object> row : empList) {
//			System.out.println(row);
//		}

////		Create Map<String,Object> store to hold our returned resultset which will be returned by execute() method.
		Map<String, Object> res = new HashMap<String, Object>();

		SimpleJdbcCall sjc1 = new SimpleJdbcCall(jdbcTemplate.getDataSource()).withProcedureName("p_employee_screen")
				.returningResultSet("empres", new BeanPropertyRowMapper<>(Employee2.class));

		MapSqlParameterSource msps = new MapSqlParameterSource()
				.addValue("p_status", "selectAllEmployees", Types.VARCHAR).addValue("firstName", null, Types.VARCHAR)
				.addValue("employeeId", null, Types.INTEGER).addValue("jobId", null, Types.VARCHAR)
				.addValue("email", null, Types.VARCHAR).addValue("hireDate", null, Types.DATE)
				.addValue("salary", null, Types.DOUBLE);

		res = sjc1.execute(msps);
		
		List<Employee2> empList = (List<Employee2>) res.get("empres");
		
		empList.forEach(x->System.out.println(x));

		return "test3";
	}
}
