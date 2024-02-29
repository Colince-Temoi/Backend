package tech.csm.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.domain.Employee;

@Controller
public class StoredProcController {

	@Autowired

	private JdbcTemplate jdbcTemplate;

	@GetMapping("/StoredProc")
	public String getRedgForm() {

//		Working with stored procedures in Spring Jdbc

//		Option 1
		/*
		 * It's useful when you need full control over creating the CallableStatement
		 * and processing the results.
		 */
		List<Employee> employeeList = jdbcTemplate.execute(new CallableStatementCreator() {

			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement pc = con.prepareCall("{call hr.p_employee_screen(?,?,?,?,?,?,?,?)}");
				pc.setString(1, "selectAllEmployees");
				pc.setString(2, null);
				pc.setInt(3, Types.NULL);
				pc.setString(4, null);
				pc.setString(5, null);
				pc.setDate(6, null);
				pc.setDouble(7, Types.NULL);

//				Register out parameters
				pc.registerOutParameter(8, Types.VARCHAR);

				return pc;
			}
		}, new CallableStatementCallback<List<Employee>>() {

			@Override
			public List<Employee> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Employee> employees = new ArrayList<>();

				// Retrieve the result set
				ResultSet rs = cs.executeQuery();

				// Iterate over the result set and create Employee objects
				while (rs.next()) {
					Employee employee = new Employee();
					employee.setEmployeeId(rs.getInt("emp_id"));
					employee.setLastName(rs.getString("first_name"));
					employee.setJobId(rs.getString("job_id"));
					employee.setHireDate(rs.getDate("hire_date"));
					employee.setSalary(rs.getDouble("salary"));

					employees.add(employee);
				}

				// Close the result set and statement
				rs.close();
				cs.close();

				return employees;
			}
		});

//		employeeList.forEach((x) -> System.out.println(x));

//		Option 2 
		/*
		 * Simpler when you don't need fine-grained control over statement creation.
		 * Internally creates a CallableStatementCreator for you.
		 */

		List<Employee> employeeList1 = jdbcTemplate.execute("{call hr.p_employee_screen(?,?,?,?,?,?,?,?)}",
				new CallableStatementCallback<List<Employee>>() {
					@Override
					public List<Employee> doInCallableStatement(CallableStatement cs)
							throws SQLException, DataAccessException {
						cs.setString(1, "selectAllEmployees");
						cs.setString(2, null);
						cs.setInt(3, Types.NULL);
						cs.setString(4, null);
						cs.setString(5, null);
						cs.setDate(6, null);
						cs.setDouble(7, Types.NULL);

//				Register out parameters
						cs.registerOutParameter(8, Types.VARCHAR);

						ResultSet rs = cs.executeQuery();
						List<Employee> employees = new ArrayList<>();
						while (rs.next()) {
							Employee employee = new Employee();
							employee.setEmployeeId(rs.getInt("emp_id"));
							employee.setLastName(rs.getString("first_name"));
							employee.setJobId(rs.getString("job_id"));
							employee.setHireDate(rs.getDate("hire_date"));
							employee.setSalary(rs.getDouble("salary"));

							employees.add(employee);
						}
						return employees;
					}
				});

//		employeeList1.forEach(x -> System.out.println(x));

//		Insert Operation
//		String outMessage = jdbcTemplate.execute("{call hr.p_employee_screen(?,?,?,?,?,?,?,?)}", new CallableStatementCallback<String>() {
//		    @Override
//		    public String doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
//		        cs.setString(1, "insEmployee");
//		        cs.setString(2, "John"); // Dummy first name
//		        cs.setInt(3, Types.NULL);
//		        cs.setString(4, "IT_PROG"); // Dummy job ID
//		        cs.setString(5, "john.doe@example.com"); // Dummy email
//		        cs.setDate(6, new java.sql.Date(System.currentTimeMillis())); // Current date
//		        cs.setDouble(7, 12345.67); // Dummy salary
//		        cs.registerOutParameter(8, Types.VARCHAR);
//
//		        cs.execute();
//		        return cs.getString(8); // Get the output message
//		    }
//		});
//
//		System.out.println(outMessage);

//		Update operation
//		String resultMessage = jdbcTemplate.execute(new CallableStatementCreator() {
//			@Override
//			public CallableStatement createCallableStatement(Connection con) throws SQLException {
//				CallableStatement cs = con.prepareCall("{call hr.p_employee_screen(?,?,?,?,?,?,?,?)}");
//				cs.setString(1, "UpdateEmployeeById");
//				cs.setInt(3, 4); // Dummy employee ID to update
//				cs.setString(2, "John"); // Updated first name
//				cs.setString(4, "IT_PROG"); // Updated job id
//				cs.setString(5, "john@example.com"); // Updated email
//				cs.setDate(6, new java.sql.Date(System.currentTimeMillis())); // Updated hire date (today)
//				cs.setDouble(7, 90000.0); // Updated salary
//				cs.registerOutParameter(8, Types.VARCHAR);
//				return cs;
//			}
//		}, new CallableStatementCallback<String>() {
//			@Override
//			public String doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
//				cs.executeUpdate();
//				return cs.getString(8); // Return the output message
//			}
//		});

//		System.out.println(resultMessage);

//		Delete Operation
//		String resultMessage = jdbcTemplate.execute(new CallableStatementCreator() {
//			@Override
//			public CallableStatement createCallableStatement(Connection con) throws SQLException {
//				CallableStatement cs = con.prepareCall("{call hr.p_employee_screen(?,?,?,?,?,?,?,?)}");
//				cs.setString(1, "DeleteEmployeeById");
//				cs.setInt(3, 4); // Employee ID to delete
//				cs.setNull(2, Types.VARCHAR); // Null for first name
//				cs.setNull(4, Types.VARCHAR); // Null for job id
//				cs.setNull(5, Types.VARCHAR); // Null for email
//				cs.setNull(6, Types.DATE); // Null for hire date
//				cs.setNull(7, Types.DOUBLE); // Null for salary
//				cs.registerOutParameter(8, Types.VARCHAR);
//				return cs;
//			}
//		}, new CallableStatementCallback<String>() {
//			@Override
//			public String doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
//				cs.execute();
//				return cs.getString(8);
//			}
//		});
//
//		System.out.println(resultMessage);

//		Delete Operation: Approach 2-Setting NULL values
		/*
		 * cs.setNull(2, Types.VARCHAR);: This method explicitly sets the parameter at the given index to NULL. It's clear and explicit in its intention, indicating that you're setting a parameter to be NULL, and it also specifies the SQL type explicitly.
		 * cs.setString(4, null);: This method sets the parameter at the given index to whatever value is passed. In this case, null is passed, indicating that the parameter should be set to NULL. This method relies on the SQL type of the parameter being VARCHAR, which might be clear from the context, but it's not explicitly stated.
		 * In general, cs.setNull(2, Types.VARCHAR); is more explicit and clearer in intention. It explicitly states that you're setting the parameter to NULL and also specifies the SQL type. This can make the code more readable and less prone to errors, especially in cases where the SQL type might not be obvious from the context.
		 * Therefore, cs.setNull(2, Types.VARCHAR); is recommended when setting a parameter to NULL, especially in scenarios where clarity and explicitness are important.
		 * */
		
		String resultMessage = jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				CallableStatement cs = con.prepareCall("{call hr.p_employee_screen(?,?,?,?,?,?,?,?)}");
				cs.setString(1, "DeleteEmployeeById");
				cs.setInt(3, 5); // Employee ID to delete
				cs.setNull(2, Types.VARCHAR); // Null for first name
				cs.setNull(4, Types.VARCHAR); // Null for job id
				cs.setNull(5, Types.VARCHAR); // Null for email
				cs.setNull(6, Types.DATE); // Null for hire date
				cs.setNull(7, Types.DOUBLE); // Null for salary
				cs.registerOutParameter(8, Types.VARCHAR);
				return cs;
			}
		}, new CallableStatementCallback<String>() {
			@Override
			public String doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.execute();
				return cs.getString(8);
			}
		});

		System.out.println(resultMessage);

		return "test3";
	}

}
