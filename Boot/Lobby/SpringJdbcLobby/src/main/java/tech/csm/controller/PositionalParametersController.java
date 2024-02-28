package tech.csm.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.domain.Department;
import tech.csm.domain.Employee;

@Controller
public class PositionalParametersController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/positionalParams")
	public String getRedgForm() {

//		Passing Positional parameters: This can be done anywhere in a query where it is required
		List<Employee> e = jdbcTemplate.query(
				"SELECT EMPLOYEE_ID,LAST_NAME,SALARY,JOB_ID FROM hr.employees where JOB_ID=?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, "IT_PROG");

					}
				}, new ResultSetExtractor<List<Employee>>() {

					@Override
					public List<Employee> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Employee employee = null;
						Department department = null;
						List<Employee> eList = new ArrayList<>();

						while (rs.next()) {

							employee = new Employee();

							employee.setEmployeeId(rs.getInt(1));
							employee.setLastName(rs.getString(2));
							employee.setSalary(rs.getDouble(3));
							employee.setJobId(rs.getString(4));
//							employee.setHireDate(rs.getDate(5));
//
//							department = new Department();
//							department.setDeptId(rs.getInt(6));
//							department.setDeptName(rs.getString(7));
//
//							employee.setDepartment(department);

							eList.add(employee);
						}
						return eList;
					}
				});

		List<Employee> eList = jdbcTemplate
				.query("Select e.EMPLOYEE_ID,e.LAST_NAME,e.SALARY,e.JOB_ID,e.HIRE_DATE,e.DEPARTMENT_ID\r\n"
						+ "from employees e\r\n" + "where salary > ? and job_id=?", new PreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setInt(1, 5000);
								ps.setString(2, "IT_PROG");
							}
						}, new ResultSetExtractor<List<Employee>>() {

							@Override
							public List<Employee> extractData(ResultSet rs) throws SQLException, DataAccessException {

								Employee employee = null;
								Department department = null;
								List<Employee> eList = new ArrayList<>();

								while (rs.next()) {

									employee = new Employee();

									employee.setEmployeeId(rs.getInt(1));
									employee.setLastName(rs.getString(2));
									employee.setSalary(rs.getDouble(3));
									employee.setJobId(rs.getString(4));
									employee.setHireDate(rs.getDate(5));

									eList.add(employee);

								}
								return eList;
							}
						});

		List<Employee> employeeList = jdbcTemplate
				.query("Select e.EMPLOYEE_ID,e.LAST_NAME,e.SALARY,e.JOB_ID,e.HIRE_DATE,e.DEPARTMENT_ID\r\n"
						+ "from employees e\r\n" + "where salary > ?", new PreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setInt(1, 15000);

							}
						}, new RowMapper<Employee>() {

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

								return employee;
							}
						});

		List<Map<String, Object>> departmentList = jdbcTemplate.queryForList(
				"SELECT department_id, COUNT(*) AS num_employees " + "FROM departments " + "GROUP BY department_id");

		/*
		 * for (Map<String, Object> map : departmentList) { for (Map<String, Object>
		 * department : departmentList) { System.out.println("Department ID: " +
		 * department.get("department_id")); System.out.println("Number of Employees: "
		 * + department.get("num_employees")); } }
		 */

		/*
		 * for (Employee employee : departmentList) { System.out.println(employee);
		 * 
		 * }
		 */

//		DML: Insert
		KeyHolder kh = new GeneratedKeyHolder();

		/*
		 * Integer rc = jdbcTemplate.update(new PreparedStatementCreator() {
		 * 
		 * @Override public PreparedStatement createPreparedStatement(Connection con)
		 * throws SQLException { PreparedStatement ps = con.prepareStatement(
		 * "INSERT INTO emp1(first_name, email,job_id,hire_date,salary) \r\n" +
		 * "VALUES(?, ?,?,?,?)", java.sql.Statement.RETURN_GENERATED_KEYS);
		 * 
		 * ps.setString(1, "Marvin"); ps.setString(2, "marv@gmail.com"); ps.setString(3,
		 * "ST_CLERK"); try { ps.setDate(4, new Date(new
		 * SimpleDateFormat("dd-MM-yyyy").parse("14-01-2005").getTime())); } catch
		 * (SQLException e) { e.printStackTrace(); } catch (ParseException e) {
		 * e.printStackTrace(); } ps.setDouble(5, 7500.00);
		 * 
		 * return ps; } }, kh);
		 * 
		 * if (rc == 1)
		 * 
		 * System.out.println(rc + " employee saved with Id: " + kh.getKey());
		 */

//		DML: Update
		/*
		 * int rc =
		 * jdbcTemplate.update("UPDATE emp1 SET job_id = ?, salary = ? WHERE emp_id = ?"
		 * , new Object[] { "FI_ACCOUNT", 89654.87, 2 });
		 * 
		 * System.out.println(rc + " record updated with id " + kh.getKey());
		 */

//		DML: Delete
		jdbcTemplate.update("DELETE FROM emp1 WHERE emp_id = ?", new Object[] { 2 });
		

		return "test2";
	}
}
