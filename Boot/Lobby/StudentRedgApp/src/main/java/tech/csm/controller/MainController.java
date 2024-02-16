package tech.csm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import tech.csm.domain.Branch;
import tech.csm.service.BranchService;

@Controller
public class MainController {
	
	@Autowired
	private BranchService branchService;

	@GetMapping("/getRedgForm")
	public String getRedgForm(Model model) {
//		current year
		Integer currYr = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

//		Create AL store
		ArrayList<Integer> dateList = new ArrayList<Integer>();

//		Make a loop to add a list of years from 2020 to 2024
		int i;
		for ( i = currYr; i > currYr - 4; i--) {
			dateList.add(i);
		}
		
//		Create Branch List
		List<Branch> branchList = branchService.getFindAllBranches();
				
//		Adding objects to Model-Jsp page
		model.addAttribute("dateList", dateList);
		model.addAttribute("branchList", branchList);
		return "registration";
	}
}
