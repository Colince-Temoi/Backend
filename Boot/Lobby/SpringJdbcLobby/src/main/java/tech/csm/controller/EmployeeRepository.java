package tech.csm.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.javafaker.Faker;

import tech.csm.domain.Employee;
import tech.csm.domain.Employee2;
import tech.csm.domain.Job;
import tech.csm.domain.JobHistory;

@RestController
@RequestMapping("/postData1")
public class EmployeeRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private Validator validator;

    private ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(Faker::new);

    
        @PostMapping
        public void insertEmployees() {
            Faker faker = new Faker();
            String insertQuery = "INSERT INTO emp1 (first_name, email, job_id, hire_date, salary) "
                    + "VALUES (:firstName, :email, :jobId, :hireDate, :salary)";

            List<SqlParameterSource> parameterSources = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                String firstName = faker.name().firstName();
                String email = faker.internet().emailAddress();
                String jobId = faker.random().hex(5);
                Date hireDate = new Date(faker.date().past(3650, TimeUnit.DAYS).getTime());
                Double salary = faker.random().nextDouble() * (150000 - 50000) + 50000;

                // Validate data
                if (firstName == null || firstName.isEmpty()) {
                    throw new IllegalArgumentException("First name cannot be empty");
                }
                if (email == null || email.isEmpty() || !email.contains("@")) {
                    throw new IllegalArgumentException("Invalid email address");
                }
                if (jobId == null || jobId.isEmpty()) {
                    throw new IllegalArgumentException("Job ID cannot be empty");
                }
                if (salary == null || salary < 0) {
                    throw new IllegalArgumentException("Salary cannot be negative");
                }

                SqlParameterSource parameterSource = new MapSqlParameterSource()
                        .addValue("firstName", firstName)
                        .addValue("email", email)
                        .addValue("jobId", jobId)
                        .addValue("hireDate", hireDate)
                        .addValue("salary", salary);

                parameterSources.add(parameterSource);
            }

            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.batchUpdate(insertQuery, parameterSources.toArray(new SqlParameterSource[0]), keyHolder);

            List<Map<String, Object>> keys = keyHolder.getKeyList();
            for (Map<String, Object> key : keys) {
                System.out.println("Generated Key: " + key.get("GENERATED_KEY"));
            }
        }
	}
