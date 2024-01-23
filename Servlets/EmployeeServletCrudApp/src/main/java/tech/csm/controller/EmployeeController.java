package tech.csm.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tech.csm.service.DepartmentService;
import tech.csm.service.DepartmentServiceImpl;
import tech.csm.service.EmployeeService;
import tech.csm.service.EmployeeServiceImpl;
import tech.csm.util.DBUtil;

public class EmployeeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DepartmentService departmentService;
	private EmployeeService employeeService;

	public EmployeeController() {
		departmentService = new DepartmentServiceImpl();
		employeeService=new EmployeeServiceImpl();
	}

	@Override
	public void init() throws ServletException {
		DBUtil.getSessionFactory();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");

		PrintWriter pw = response.getWriter();

		pw.println(DBUtil.getSessionFactory().openSession());
		
//		Get the end-point
		 String endpoint = request.getServletPath();
		 
//		 System.out.println(departmentService.getAllDepartments());
		 
//		 System.out.println("Endpoint: "+endpoint);
		 if (endpoint.equals("/")) {
			 String endpoint2 = request.getServletPath();
			 System.out.println("Endpoint: "+endpoint2);
			 request.setAttribute("deptlist", departmentService.getAllDepartments());
			 
			 request.setAttribute("emplist", employeeService.getAllEmployees());
//				Get request dispatcher object
				RequestDispatcher rd = request.getRequestDispatcher("/empredg.jsp");
				
				rd.forward(request, response);
		}
		
		


	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
