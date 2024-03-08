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

	@PostMapping("/saveEmp")
	public String saveEmployee(@RequestBody Employee jsonData, Model model) {
		
		System.out.println(jsonData);
		
//		System.out.println(employee);
		
//		Integer id = employeeService.saveEmployee(employee);
		return "redirect:/emp";
	}
}
