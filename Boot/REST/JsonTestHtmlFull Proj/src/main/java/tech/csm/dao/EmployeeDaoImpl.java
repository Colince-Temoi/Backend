package tech.csm.dao;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import tech.csm.domain.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	@Autowired
	private DataSource dataSource;

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

}
