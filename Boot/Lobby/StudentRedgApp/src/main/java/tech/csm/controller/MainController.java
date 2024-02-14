package tech.csm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/getRedgForm")
	public String getRedgForm() {
//		current year
		Integer currYr = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

//		Create AL store
		ArrayList<Integer> dateList = new ArrayList<Integer>();

//		Make a loop to add a list of years from 2020 to 2024
		int i;
		for ( i = currYr; i > currYr - 4; i--) {
			dateList.add(i);
		}
		return "registration";
	}
}
