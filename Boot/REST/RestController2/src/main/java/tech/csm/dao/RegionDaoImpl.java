package tech.csm.dao;

import java.sql.PreparedStatement;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tech.csm.domain.Country;
import tech.csm.domain.Location;
import tech.csm.domain.Region;

@Repository
public class RegionDaoImpl implements RegionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Integer saveAddress(Region region) {
        // Insert into regions table
        SimpleJdbcInsert regionInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("regions1").usingGeneratedKeyColumns("REGION_ID");
        Number regionId = regionInsert.executeAndReturnKey(Map.of("region_name", region.getRegionName()));
        region.setRegionId(regionId.intValue());

        // Insert into countries table
        Country country = region.getCountry();
        SimpleJdbcInsert countryInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("countries1");
        countryInsert.execute(Map.of("country_id", country.getCountryId(), 
                                      "country_name", country.getCountryName(), 
                                      "region_id", regionId));

        // Insert into locations table
        Location location = country.getLocation();
        SimpleJdbcInsert locationInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("locations1");
        locationInsert.execute(Map.of("street_address", location.getStreetAddress(), 
                                       "postal_code", location.getPostalCode(), 
                                       "city", location.getCity(), 
                                       "state_province", location.getStateProvince(), 
                                       "country_id", country.getCountryId()));

        return regionId.intValue();
    }
    
//    private final SimpleJdbcInsert regionInsert;
//    private final SimpleJdbcInsert countryInsert;
//    private final SimpleJdbcInsert locationInsert;
//    
//    @Autowired
//    public RegionDaoImpl(JdbcTemplate jdbcTemplate) {
//        this.regionInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
//                .withTableName("regions1")
//                .usingGeneratedKeyColumns("region_id");
//
//        this.countryInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
//                .withTableName("countries1");
//
//        this.locationInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
//                .withTableName("locations1");
//
//        new NamedParameterJdbcTemplate(jdbcTemplate);
//    }
//
//    @Override
//    public Integer saveAddress(Region region) {
//        // Insert into regions table
//        MapSqlParameterSource regionParameters = new MapSqlParameterSource()
//            .addValue("region_name", region.getRegionName());
//
//        Number regionId = regionInsert.executeAndReturnKey(regionParameters);
//        region.setRegionId(regionId.intValue());
//
//        // Insert into countries table
//        Country country = region.getCountry();
//        MapSqlParameterSource countryParameters = new MapSqlParameterSource()
//            .addValue("country_id", country.getCountryId())
//            .addValue("country_name", country.getCountryName())
//            .addValue("region_id", regionId.intValue());
//
//        countryInsert.execute(countryParameters);
//
//        // Insert into locations table
//        Location location = country.getLocation();
//        MapSqlParameterSource locationParameters = new MapSqlParameterSource()
//            .addValue("street_address", location.getStreetAddress())
//            .addValue("postal_code", location.getPostalCode())
//            .addValue("city", location.getCity())
//            .addValue("state_province", location.getStateProvince())
//            .addValue("country_id", country.getCountryId());
//
//        locationInsert.execute(locationParameters);
//
//        return regionId.intValue();
//    }

    @Override
    public Region getRegionById(Integer regionId) {
        String query = "SELECT * FROM regions1 WHERE region_id = ?";
        RowMapper<Region> rowMapper = new BeanPropertyRowMapper<>(Region.class);
        
        Region region = jdbcTemplate.queryForObject(query, rowMapper, regionId);
        
        return region;
    }
    
    
    
//    @Override
//    public Integer saveAddress(Region region) {
//        // Insert into regions table
//        String regionQuery = "INSERT INTO regions1 (region_name) VALUES (?)";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(regionQuery, new String[] { "REGION_ID" });
//            ps.setString(1, region.getRegionName());
//            return ps;
//        }, keyHolder);
//
//        Integer regionId = keyHolder.getKey().intValue();
//        region.setRegionId(regionId); // Ensure the region object has the generated ID
//
//        // Insert into countries table
//        Country country = region.getCountry();
//        String countryQuery = "INSERT INTO countries1 (country_id, country_name, region_id) VALUES (?, ?, ?)";
//        jdbcTemplate.update(countryQuery, 
//            country.getCountryId(), // Use the provided countryId
//            country.getCountryName(), 
//            regionId // Use the generated regionId
//        );
//
//        // Insert into locations table
//        Location location = country.getLocation();
//        String locationQuery = "INSERT INTO locations1 (street_address, postal_code, city, state_province, country_id) VALUES (?, ?, ?, ?, ?)";
//        jdbcTemplate.update(locationQuery, 
//            location.getStreetAddress(), 
//            location.getPostalCode(), 
//            location.getCity(), 
//            location.getStateProvince(), 
//            country.getCountryId() // Use the provided countryId
//        );
//
//        return regionId;
//    }
    
    @Override
    public Integer updateAddress(Region region) {
        String sql = "UPDATE regions1 SET region_name = :regionName WHERE region_id = :regionId";
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(region);
        int rowsAffected = new NamedParameterJdbcTemplate(jdbcTemplate).update(sql, paramSource);

        Country country = region.getCountry();
        sql = "UPDATE countries1 SET country_name = :countryName WHERE country_id = :countryId";
        paramSource = new BeanPropertySqlParameterSource(country);
        rowsAffected += new NamedParameterJdbcTemplate(jdbcTemplate).update(sql, paramSource);

        Location location = country.getLocation();
        sql = "UPDATE locations1 SET street_address = :streetAddress, postal_code = :postalCode, city = :city, state_province = :stateProvince WHERE location_id = :locationId";
        paramSource = new BeanPropertySqlParameterSource(location);
        rowsAffected += new NamedParameterJdbcTemplate(jdbcTemplate).update(sql, paramSource);

        return rowsAffected;
    }
    
    /*
     * This code will delete the region with the given id, along with its associated country and location. Please note that this is a cascading delete operation, which means it will delete the child records first (locations and countries) before deleting the parent record (region).
     * */
    @Override
    public Integer deleteAddress(Integer regionId) {
        String sql = "DELETE FROM locations1 WHERE country_id IN (SELECT country_id FROM countries1 WHERE region_id = :regionId)";
        SqlParameterSource paramSource = new MapSqlParameterSource("regionId", regionId);
        int rowsAffected = new NamedParameterJdbcTemplate(jdbcTemplate).update(sql, paramSource);

        sql = "DELETE FROM countries1 WHERE region_id = :regionId";
        rowsAffected += new NamedParameterJdbcTemplate(jdbcTemplate).update(sql, paramSource);

        sql = "DELETE FROM regions1 WHERE region_id = :regionId";
        rowsAffected += new NamedParameterJdbcTemplate(jdbcTemplate).update(sql, paramSource);

        return rowsAffected;
    }

  }