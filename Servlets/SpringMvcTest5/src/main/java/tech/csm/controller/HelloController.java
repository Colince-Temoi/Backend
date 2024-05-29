package tech.csm.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HelloController extends ParameterizableViewController{
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("InsideController");
		String  name = request.getParameter("name");
		Map<String, String> m = new HashMap<>();
//		Your required ouput you can store under map object
		m.put("msg", "Hello..."+name +" using SimpleUrlHandlerMapping and ...ParameterizableViewController");
		
		return new ModelAndView(getViewName(), m);
	}

}
