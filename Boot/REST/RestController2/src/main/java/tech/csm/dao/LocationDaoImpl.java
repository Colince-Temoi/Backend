package tech.csm.dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tech.csm.domain.Location;

import java.util.List;

@Repository
public class LocationDaoImpl implements LocationsDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
    public List<Location> getAllLocations() {
        String sql = "SELECT * FROM locations1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Location.class));
    }
	
	@Override
    public Location getLocationById(int id) {
        System.out.println("Get location by id DAO called");
        String sql = "SELECT * FROM locations1 WHERE LOCATION_ID = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Location.class), id);
    }
	
    }