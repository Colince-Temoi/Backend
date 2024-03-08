package tech.csm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tech.csm.domain.Department;
import tech.csm.domain.Employee;
import tech.csm.service.DepartmentService;
import tech.csm.service.EmployeeSevice;

@Controller
@RequestMapping(path = "/emp")
public class MainController {

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private EmployeeSevice employeeService;

	@GetMapping
	public String getRedgForm() {

		return "index";
	}

	@GetMapping("/getFormData")
	@ResponseBody
	public List<Department> getDepartments() {

		List<Department> deptList = departmentService.findAllDepartments();

		return deptList;
	}
	
	/* Snippet B
	 * Controller Method: @PostMapping("/saveEmp") and @RequestParam String jsonData: 
	 * The @RequestParam annotation informs Spring to extract a parameter named "jsonData" from the request. 
	 * In this context, it's not interpreting the data as JSON. 
	 * It's just grabbing the raw string value and passing it to your method.
	 * */
	
// Snippet B
	/*
	@PostMapping("/saveEmp")
	public String saveEmployee(@RequestParam String jsonData, Model model) {
		
		System.out.println(jsonData);
		
//		Integer id = employeeService.saveEmployee(employee);
		return "redirect:/emp";
	}
	*/
	
	/* Snippet A
	 * Controller Method: @PostMapping("/saveEmp") and @RequestBody Employee jsonData: 
	 * The @RequestBody annotation signals Spring to expect a JSON object in the request body, and it automatically deserializes (unpacks) that JSON into an Employee object. 
	 * This mapping is seamless because Snippet A has already labeled the data as JSON and structured it accordingly.
	 * */
	
//	Snippet A
	@PostMapping("/saveEmp")
	public String saveEmployee(@RequestBody Employee employee, Model model) {
		
		System.out.println(employee);
		
//		Integer id = employeeService.saveEmployee(employee);
		return "redirect:/emp";
	}
	
	/* Key  differences and why they matter
	 * Controller Annotations: @RequestBody and @RequestParam serve distinct purposes:
@RequestBody is designed for receiving and deserializing JSON objects, hence its compatibility with Snippet A.
@RequestParam is used for extracting individual parameters from the request, which aligns with the form-like data structure in Snippet B.

*Choosing the Right Approach:

If you want to send and receive JSON data directly, Snippet A and its corresponding controller configuration are the way to go. This provides a cleaner and more type-safe approach when working with JSON.
If you're dealing with simple key-value pairs or form-like submissions, Snippet B and @RequestParam are suitable.

*/
	
}
