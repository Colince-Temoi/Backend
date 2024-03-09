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

import tech.csm.domain.Collage;
import tech.csm.domain.Employee;
import tech.csm.service.CollageService;
import tech.csm.service.EmployeeSevice;

@Controller
@RequestMapping(path = "/student")
public class MainController {

	@Autowired
	private CollageService collageService;

	@Autowired
	private EmployeeSevice employeeService;

	@GetMapping
	public String getRedgForm() {

		return "index";
	}

	@GetMapping("/getFormData")
	@ResponseBody
	public List<Collage> getCollages() {

		List<Collage> collageList = collageService.findAllCollages();

		return collageList;
	}
	
	/*
	 * @RequestBody Employee jsonData  expects the raw request body to contain valid JSON representing an Employee object.  
	 * Spring's message converters will attempt to automatically deserialize the JSON into an Employee object for you.
	 * */
	
	@PostMapping("/saveEmp")
	public String saveEmployee(@RequestBody Employee employee, Model model) {
		
		String msg = employeeService.saveEmployee(employee);
		System.out.println(msg);
		return "redirect:/emp";
	}
	
	
}
