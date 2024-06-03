package tech.csm.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HelloController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Map to hold data for the view
        Map<String, String> model = new HashMap<>();

        // Get the name from the request parameter
        String name = request.getParameter("name");

        // Add the name to the model
        model.put("name", name);

        // Return the appropriate view with the model
        return new ModelAndView("success", "model", model);
    }
}