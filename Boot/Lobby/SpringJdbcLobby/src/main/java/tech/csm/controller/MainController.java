package tech.csm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/getData")
	public String getRedgForm() {
		Double salary = jdbcTemplate.queryForObject("SELECT salary FROM hr.employees where employee_id='101'", Double.class);
		System.out.println(salary);
		return "test";
	}
}
