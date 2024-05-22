package tech.csm.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.csm.domain.Product;

//@RestController
@Controller
@RequestMapping("/")
public class JsonTestController {

	// Define 'getForm' method to return the 'test3' form view.
    @GetMapping(value = "/test3")
    public String getForm() {
        return "test";
    }
    
    // Endpoint to handle the incoming JSON data from the client.
    @PostMapping(value = "/product")
    public ResponseEntity<String> handleProductData(@RequestBody List<Product> products) {
        System.out.println("Received products: " + products);
        return ResponseEntity.ok("Data received successfully!");
    }
}



//package tech.csm.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import tech.csm.domain.Employee;
//
//@Controller
//@RequestMapping
//public class JsonTestController {
//
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//
//	@GetMapping("/JsonTest")
//	public String getRedgForm() {
//
//		return "test";
//	}
//
//	 @PostMapping("/saveData")
//	    public void saveData(@RequestParam String jsonData) {
//	        try {
//	        	System.out.println(jsonData);
//	            // Create an ObjectMapper instance
//	            ObjectMapper objectMapper = new ObjectMapper();
//	            
//	            // Parse the JSON data into an array of Employee objects
//	            Employee[] employees = objectMapper.readValue(jsonData, Employee[].class);
//	            
//	            // Print the received Employee objects
//	            for (Employee employee : employees) {
//	                System.out.println("Received Employee object: " + employee);
//	                // You can perform further actions with the employee object, such as saving it to a database
//	            }
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            // Handle exception appropriately
//	        }
//	    }
//}
