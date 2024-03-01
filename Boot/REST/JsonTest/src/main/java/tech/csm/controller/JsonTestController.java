package tech.csm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import tech.csm.domain.Employee;

@Controller
@RequestMapping
public class JsonTestController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/JsonTest")
	public String getRedgForm() {

		return "test";
	}

	@PostMapping("/saveData")
	public void saveData(@RequestParam String jsonData) {
		try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            
            // Parse the JSON data into an Employee object
            Employee employee = objectMapper.readValue(jsonData, Employee.class);
            
            // Print the received Employee object
            System.out.println("Received Employee object: " + employee);
            
            // You can perform further actions with the employee object, such as saving it to a database
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
	}
}
