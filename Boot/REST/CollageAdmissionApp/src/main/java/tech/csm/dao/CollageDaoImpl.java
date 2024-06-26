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

import tech.csm.domain.Collage;

@Repository
public class CollageDaoImpl implements Collagedao {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcCall simpleJdbcCall;

	@Override
	public List<Collage> findAllCollages() {

//	Create Map<String,Object> store to hold our returned resultset which will be returned by execute() method.
		Map<String, Object> res = new HashMap<String, Object>();
		simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("p_collage_screen")
				.returningResultSet("collegesres", new BeanPropertyRowMapper<>(Collage.class));

		MapSqlParameterSource msps = new MapSqlParameterSource().addValue("p_status", "selectAllCollages")
				.addValue("p_collageId", null, Types.INTEGER);

		res = simpleJdbcCall.execute(msps);

		List<Collage> collageList = (List<Collage>) res.get("collegesres");
		
		return collageList;

	}

	@Override
	public Collage getCollageById(Integer collageId) {
	    simpleJdbcCall = new SimpleJdbcCall(dataSource)
	            .withProcedureName("p_collage_screen")
	            .returningResultSet("collage", new BeanPropertyRowMapper<>(Collage.class));

	    MapSqlParameterSource msps = new MapSqlParameterSource()
	            .addValue("p_collageId", collageId, Types.INTEGER)
	            .addValue("p_status", "selectCollageById", Types.VARCHAR);

	    Map<String, Object> res = simpleJdbcCall.execute(msps);

	    List<Collage> collageList = (List<Collage>) res.get("collage");

	    return collageList.isEmpty() ? null : collageList.get(0);
	}




}
