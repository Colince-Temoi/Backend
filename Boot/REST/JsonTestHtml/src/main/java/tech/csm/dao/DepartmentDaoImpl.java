package tech.csm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import tech.csm.domain.Department;

@Repository
public class DepartmentDaoImpl implements Departmentdao {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcCall simpleJdbcCall;

	@Override
	public List<Department> findAllDepartments() {

//	Create Map<String,Object> store to hold our returned resultset which will be returned by execute() method.
		Map<String, Object> res = new HashMap<String, Object>();
		simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("p_depts_screen")
				.returningResultSet("deptres", new BeanPropertyRowMapper<>(Department.class));

		MapSqlParameterSource msps = new MapSqlParameterSource().addValue("p_status", "selectAllDepartments");

		res = simpleJdbcCall.execute(msps);

		List<Department> deptList = (List<Department>) res.get("deptres");

		deptList.forEach(x -> System.out.println(x));
		return deptList;

	}

}
