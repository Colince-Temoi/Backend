package tech.csm.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.csm.domain.Employee;
import tech.csm.service.EmployeeSevice;

/*Rest controller
 * With this you can communicate with other apps as it is enabled with Cross Platform Communication.
 * Browser tab can generate only GET request
 * Use Postman client as it can generate any type of request.
 * */

@Controller
@RestController
@RequestMapping(value = "/emp") //you can also use path attribute instead of value. It one and the same thing.
public class MyRestController {
	
	@Autowired
	private EmployeeSevice employeeSevice;
	
	
	@GetMapping
	public List<Employee> getAllEmps() {
		System.out.println("executing!!");

		return employeeSevice.getAllEmployees();
		
	}
	
	@GetMapping(path = "{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
//	@GetMapping("/{id}") // Works as well
    public Employee getEmployeeById(@PathVariable (name="id") int empId) {	
        return employeeSevice.getEmployeeById(empId);
    }
	
//	Receiving/consuming a single Stringified Employee object from the client who is a producer in this case.
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public String saveEmployee(@RequestBody Employee em) {
		System.out.println(em);
		return "Emp saved successfully!";
	}
	
//	Receiving/consuming an array of Stringified Employee objects from the client who is a producer in this case.
	@PostMapping( value = "/batch", consumes = {MediaType.APPLICATION_JSON_VALUE})
//	public String saveEmployees(@RequestBody Employee[] ems) { //working also
		public String saveEmployees(@RequestBody List<Employee>  ems) {
//		System.out.println(Arrays.toString(ems)); // working also
		System.out.println(ems);
		return "Employees saved successfully!";
		
	}
	
	
	
	
	
	
	
/*  Alternative 1 to get an Employee by id
 * path = "{id}" || /{id} represents the incoming id
 * @PathVariable datatype id - to bind the id value so that we can use it in our method body.
 * Note: we are using the same name, id, hence no ambiguity between the path and the method reference. 
 * */
//	@GetMapping(path = "{id}")
//	@GetMapping("/{id}") // Works as well
//    public Employee getEmployeeById(@PathVariable int id) {	
//        return employeeSevice.getEmployeeById(id);
//    }
	
	
	/*  Alternative 2 to get an Employee by id
	 * path = "{id}" || /{id} represents the incoming id
	 * @PathVariable datatype id - to bind the id value so that we can use it in our method body.
	 * Note: name = "id" in @PathVariable annotation explicitly specifies the name of the path variable to bind to the method parameter, ensuring clarity
	 * */
//		@GetMapping(path = "{id}")
//		@GetMapping("/{id}") // Works as well
//	    public Employee getEmployeeById(@PathVariable (name="id") int empId) {	
//	        return employeeSevice.getEmployeeById(empId);
//	    }
		
		
		/*  Alternative 3 to get an Employee by id
		 * Use @RequestParam when the parameter is a query parameter rather than a part of the path. 
		 * This annotation binds the value of the query parameter to the method parameter.
		 * With this setup, your request URL would look like /employees?id=123.
		 *  */
//	@GetMapping("/get")
//	public Employee getEmployeeById(@RequestParam("id") int id) {
//	    return employeeSevice.getEmployeeById(id);
//	}
	
	/*
	 * By default; in this alternative 3, if you don't provide the query string required K-V pair, you will get a 400
	 * Required request parameter it is making mandatory hence the reason you will get a 400 Bad Request.
	 * We are not giving request properly, but sometimes this required parameter may not come from the client always.
	 * If I don't want this parameter to come through the request as a query string k-v kind of thing then you can use one attribute like:
	 *  (defaultValue="10")
	 *  Sometimes you don't need this default attribute, you can set required attribute to false i.e., 
	 *   (required=false)
	 * In this case you will get a 500 Internal server error which is better as request is not bad, maybe something else happened. This we will see later.
	 * */
//	@GetMapping("/get")
////	public Employee getEmployeeById(@RequestParam (defaultValue="10") int id) {
//	public Employee getEmployeeById(@RequestParam (required=false) int id) {
//	    return employeeSevice.getEmployeeById(id);
//	}

		

//	@GetMapping
//	public String getAllEmps() {
//
//		return "All emp List";
//		
//	}
	
//	@PostMapping
//	public String saveEmployee() {
//		return "Emp saved successfully!";
//	}
	
	@PutMapping
	public String updateEmp() {
		return "Updating Employee";	
	}
	
	@DeleteMapping
	public String deleteEmp() {
		return "deleting Employee";	
	}
}
