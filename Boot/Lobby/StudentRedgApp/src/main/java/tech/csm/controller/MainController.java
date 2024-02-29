package tech.csm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import tech.csm.domain.Branch;
import tech.csm.domain.City;
import tech.csm.domain.Course;
import tech.csm.domain.State;
import tech.csm.domain.Student;
import tech.csm.service.BranchService;
import tech.csm.service.CityService;
import tech.csm.service.CourseService;
import tech.csm.service.StateService;
import tech.csm.service.StudentService;

@Controller
public class MainController {

	@Autowired
	private BranchService branchService;
	@Autowired
	private StateService stateService;
	@Autowired
	private CityService cityService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private StudentService studentService;

	@GetMapping("/getRedgForm")
	public String getRedgForm(Model model) {
//		current year
		Integer currYr = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

//		Create AL store
		ArrayList<Integer> dateList = new ArrayList<Integer>();

//		Make a loop to add a list of years from 2020 to 2024
		int i;
		for (i = currYr; i > currYr - 4; i--) {
			dateList.add(i);
		}

//		Fetch branches
		List<Branch> branchList = branchService.getFindAllBranches();

//		Fetch states 
		List<State> stateList = stateService.findAllStates();

//		Fetch Courses
		List<Course> courseList = courseService.FindAllCourses();
		
		 // Fetch the list of students
	    List<Student> studentList = studentService.findAllStudents(); 
	    
	    System.out.println(studentList);


//		Adding objects to Model-Jsp page
		model.addAttribute("dateList", dateList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("courseList", courseList);
	    model.addAttribute("studentList", studentList);


		return "registration";
	}

	@GetMapping("/getCitiesByState")
	public ResponseEntity<List<City>> getCitiesByState(@RequestParam Integer stateId, HttpServletResponse response)
			throws IOException {
		System.out.println("State Id is coming: " + stateId);
		List<City> cities = cityService.findCitiesByStateId(stateId);

		cities.forEach((x) -> System.out.println(x));

		// Convert the list of cities to JSON
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(cities);

		// Return the JSON as the response
		return ResponseEntity.ok(cities);
	}

	@GetMapping("/getCourseFees")
	public ResponseEntity<Double> getCourseFees(@RequestParam Integer courseId) {
		// Call the service layer to retrieve the course fees
		Double fees = courseService.getCourseFees(courseId);

		// Return the fees as a JSON response
		return ResponseEntity.ok(fees);
	}

	@PostMapping("/saveAdmissionDetails")
	public String saveAdmissionDetails(@ModelAttribute Student student, BindingResult result) {
		// Validate the form data
		if (result.hasErrors()) {
			return "registration";
		}
		System.out.println(student);

		// Persist the student data to the database
		String msg = studentService.saveStudent(student);

		// Return a success message
		return "redirect:./getRedgForm";
	}

}
