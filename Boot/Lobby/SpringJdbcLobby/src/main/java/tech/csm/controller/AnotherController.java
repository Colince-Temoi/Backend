package tech.csm.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.domain.Department;
import tech.csm.domain.Employee;

@Controller
public class AnotherController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/getSth")
	public String getRedgForm() {

//		More jdbcTemplate.query() flavors

//		Retrieving a single object
		Employee e = jdbcTemplate.query("SELECT EMPLOYEE_ID,LAST_NAME,SALARY FROM hr.employees where employee_id='101'",
				new ResultSetExtractor<Employee>() {

					@Override
					public Employee extractData(ResultSet rs) throws SQLException, DataAccessException {
						Employee employee = null;
						while (rs.next()) {

							employee = new Employee();

							employee.setEmployeeId(rs.getInt(1));
							employee.setLastName(rs.getString(2));
							employee.setSalary(rs.getDouble(3));

						}
						return employee;
					}
				});

//		Retrieving a List of Objects

		List<Employee> eList = jdbcTemplate.query(
				"Select e.EMPLOYEE_ID,e.LAST_NAME,e.SALARY,e.JOB_ID,e.HIRE_DATE,e.DEPARTMENT_ID,d.DEPARTMENT_NAME\r\n"
						+ "from employees e\r\n" + "inner join departments d\r\n" + "using(DEPARTMENT_ID)",
				new ResultSetExtractor<List<Employee>>() {

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

							department = new Department();
							department.setDeptId(rs.getInt(6));
							department.setDeptName(rs.getString(7));

							employee.setDepartment(department);

							eList.add(employee);

						}
						return eList;
					}
				});

		List<Employee> employeeList = jdbcTemplate.query(
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

//		This lite version is not bringing up Department object related data. Find out why and if their is how you can make it bring the information.
		List<Employee> emList = jdbcTemplate.query(
				"Select e.EMPLOYEE_ID,e.LAST_NAME,e.SALARY,e.JOB_ID,e.HIRE_DATE,e.DEPARTMENT_ID,d.DEPARTMENT_NAME\r\n"
						+ "from employees e\r\n" + "inner join departments d\r\n" + "using(DEPARTMENT_ID)",
				new BeanPropertyRowMapper<Employee>(Employee.class));

		/*
		 * for (Employee employee2 : emList) { System.out.println(employee2); }
		 */

		/*
		 * for (Employee employee : employeeList) { System.out.println(employee); }
		 */

//		System.out.println(e);

		/*
		 * for (Employee employee2 : eList) { System.out.println(employee2); }
		 */
		return "test1";
	}
}
