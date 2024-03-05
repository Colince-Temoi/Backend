package tech.csm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tech.csm.domain.Department;
import tech.csm.service.DepartmentService;

@Controller
@RequestMapping(path = "/emp")
public class MainController {
	
	@Autowired
	private DepartmentService departmentService;

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
}
