package tech.csm.controller;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import tech.csm.entity.Departments;
import tech.csm.entity.Employees;
import tech.csm.service.DepartmentService;
import tech.csm.service.DepartmentServiceImpl;
import tech.csm.service.EmployeeService;
import tech.csm.service.EmployeeServiceImpl;
import tech.csm.util.DBUtil;

public class EmpController extends HttpServlet {

	private DepartmentService departmentService;
	private EmployeeService employeeService;

	public EmpController() {
		departmentService = new DepartmentServiceImpl();
		employeeService=new EmployeeServiceImpl();
	}

	@Override
	public void init() throws ServletException {
		DBUtil.getSessionFactory();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();
		
		String endPoint = req.getServletPath();
		System.out.println(endPoint);
		if (endPoint.equals("/")) {
			req.setAttribute("deptList", departmentService.getAllDepartments());
			req.setAttribute("allEmps", employeeService.getAllEmps());
			
			RequestDispatcher rd = req.getRequestDispatcher("empRegdForm.jsp");
			rd.forward(req, resp);
		}
		else if(endPoint.equals("/saveEmp")) {
			System.out.println("++++++++");
			Employees emp=new Employees();
			String eId1=req.getParameter("empId");			
			emp.setName(req.getParameter("eName").trim());
			emp.setSalary(Double.parseDouble(req.getParameter("eSal")));
			try {
				emp.setHireDate(new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("ehDate")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			emp.setEmploymentType(req.getParameter("empType"));
			Departments d=new Departments();
			d.setDeptId(Integer.parseInt(req.getParameter("deptId")));
			emp.setDepartments(d);
			emp.setIsDelete("NO");
			if(!eId1.isEmpty()) {
				emp.setEmployeeId(Integer.parseInt(eId1));
			}	
			
			employeeService.saveEmp(emp);
			resp.sendRedirect("./");
			
		}
		else if(endPoint.equals("/deleteEmp")) {
			Integer eId=Integer.parseInt(req.getParameter("empId"));
			String msg=employeeService.deleteEmpById(eId);
			resp.sendRedirect("./");
			
		}
		else if(endPoint.equals("/updateEmp")) {
			Integer eId=Integer.parseInt(req.getParameter("empId"));
			Employees em=employeeService.getEmpById(eId);
			
			req.setAttribute("deptList", departmentService.getAllDepartments());
			req.setAttribute("allEmps", employeeService.getAllEmps());
			req.setAttribute("e", em);
			RequestDispatcher rd = req.getRequestDispatcher("empRegdForm.jsp");
			rd.forward(req, resp);
		}
	}
	
	@Override	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("ye sin post");
		doGet(req, resp);
	}
}
