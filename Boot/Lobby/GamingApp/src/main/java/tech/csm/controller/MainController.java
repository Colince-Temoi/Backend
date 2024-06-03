package tech.csm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/getRedgForm")
	public String getRedgForm() {
		System.out.println("working!");
		return "redgForm";
	}
}
