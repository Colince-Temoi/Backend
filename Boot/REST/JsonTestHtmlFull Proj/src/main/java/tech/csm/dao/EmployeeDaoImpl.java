package tech.csm.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import tech.csm.domain.Department;
import tech.csm.domain.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	@Autowired
	private DataSource dataSource;
	
	@Autowired

	private JdbcTemplate jdbcTemplate;

	private SimpleJdbcCall simpleJdbcCall;

	@Override
	public String saveEmployee(Employee employee) {
		simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("p_emp4").declareParameters(
				new SqlParameter("p_status", Types.VARCHAR), new SqlParameter("p_emp_name", Types.VARCHAR),
				new SqlParameter("p_emp_id", Types.INTEGER), new SqlParameter("p_hire_date", Types.DATE),
				new SqlParameter("p_sal", Types.DOUBLE), new SqlParameter("p_department_id", Types.INTEGER),
				new SqlOutParameter("o_msg", Types.VARCHAR));

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("p_emp_id", null, Types.INTEGER)
				.addValue("p_emp_name", employee.getEmpName()).addValue("p_sal", employee.getSal())
				.addValue("p_hire_date", employee.getHireDate()) // Assuming hireDate is java.sql.Date
				.addValue("p_department_id", employee.getDepartment().getDepartmentId())
				.addValue("p_status", "insEmployee"); // Assuming your procedure expects 'insEmployee'

		Map<String, Object> results = simpleJdbcCall.execute(params);

		return (String) results.get("o_msg"); 
	}

	@Override
	public List<Employee> findAllEmployees() {
	    String callString = "{call p_emp4(?,?,?,?,?,?,?)}"; 
	    
	    return jdbcTemplate.execute(callString, new CallableStatementCallback<List<Employee>>() {
	        @Override
	        public List<Employee> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
	            // Set IN parameters - note that you may not need all of these for the find all use case
	            cs.setString(1, "selAllEmployees"); // Assuming a status parameter for select
	            cs.setInt(2, 0); // Assuming an int for emp_id. Modify based on stored procedure
	            cs.setString(3, null); // p_emp_name
	            cs.setDouble(4, 0); // p_sal
	            cs.setDate(5, null);  // p_hire_date
	            cs.setInt(6, 0); // p_department_id
	            
	            // Register OUT parameter
	            cs.registerOutParameter(7, Types.VARCHAR);
	            
	            cs.execute();
	            
	            // MySQL doesn't use a cursor for result sets, so we get the ResultSet directly
	            ResultSet rs = cs.getResultSet(); 
	            
	            List<Employee> employees = new ArrayList<>();
	            while (rs.next()) {
	                Employee employee = new Employee();
	                // Populate employee object from the ResultSet
	                employee.setEmpId(rs.getInt("emp_id"));
	                employee.setEmpName(rs.getString("emp_name"));
	                employee.setSal(rs.getDouble("sal"));
	                employee.setHireDate(rs.getString("hire_date"));
	                // Assuming Department is a separate class with a constructor that takes an ID
	               Department d = new Department();
	                    d.setDepartmentId(rs.getInt("department_id"));
	                    employee.setDepartment(d); 
	                employees.add(employee);
	            }
	            
	            return employees;
	        }
	    });
	}

}
