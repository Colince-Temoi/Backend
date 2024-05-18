package tech.csm.dao;

import java.sql.PreparedStatement;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

  }