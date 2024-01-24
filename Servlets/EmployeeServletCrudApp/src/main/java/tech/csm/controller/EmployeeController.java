package tech.csm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tech.csm.entity.Departments;
import tech.csm.entity.Employees;
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
		employeeService = new EmployeeServiceImpl();
	}

	@Override
	public void init() throws ServletException {
		DBUtil.getSessionFactory();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");

		PrintWriter pw = response.getWriter();

//		pw.println(DBUtil.getSessionFactory().openSession());

//		Get the end-point
		String endpoint = request.getServletPath();

//		 System.out.println(departmentService.getAllDepartments());

//		 System.out.println("Endpoint: "+endpoint);
		if (endpoint.equals("/")) {
			String endpoint2 = request.getServletPath();
			System.out.println("Endpoint: " + endpoint2);
			request.setAttribute("deptlist", departmentService.getAllDepartments());

			request.setAttribute("emplist", employeeService.getAllEmployees());
//				Get request dispatcher object
			RequestDispatcher rd = request.getRequestDispatcher("/empredg.jsp");

			rd.forward(request, response);
		} else if (endpoint.equals("/saveEmp")) {
//			System.out.println("++++++++");
//			Create a storage to store Employee information
			Employees emp = new Employees();

//			Get data from the request object while setting the things into our store
			String eId1 = request.getParameter("empId");
			emp.setName(request.getParameter("ename").trim());
			emp.setSalary(Double.parseDouble(request.getParameter("esal")));
			try {
				emp.setHireDate(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("ehiredate")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			emp.setEmploymentType(request.getParameter("emplomenttype"));

//			Create store to store department related information
			Departments d = new Departments();

			d.setDeptId(Integer.parseInt(request.getParameter("department")));

//			Set this secondary dependency to Employee store
			emp.setDepartments(d);
			emp.setIsDelete("NO");

//			If not empty, that's when we will set; It will have a value when we hit the ./update end-point

			if (!eId1.isEmpty()) {
				emp.setEmployeeId(Integer.parseInt(eId1));
			}

			String msg = employeeService.saveEmp(emp);

			System.out.println(msg);
//			On successful saving redirect to ./
			response.sendRedirect("./");
		} else if (endpoint.equals("/deleteEmp")) {
//			System.out.println(endpoint);
			Integer eId = Integer.parseInt(request.getParameter("empId"));
//			System.out.println(eId+" is the record id to be deleted!");

//			We are expecting some String, but we cannot print it now until we have an idea on Sessions
			String msg = employeeService.deleteEmpById(eId);

//			On successful deleting redirect to ./
			response.sendRedirect("./");

		}else if(endpoint.equals("/updateEmp")) {
//			Take the id from the query string based on the key
			Integer eId=Integer.parseInt(request.getParameter("empId"));
//			Invoke getEmpBy id Service method
			Employees em=employeeService.getEmpById(eId);
			
//			Set the 3 object into the request object ready for dispatching to our form
			request.setAttribute("deptlist", departmentService.getAllDepartments());
			request.setAttribute("emplist", employeeService.getAllEmployees());
			request.setAttribute("e", em);
//			Now, when you hit this /update end-point these 3 things will be present with us in the form
			RequestDispatcher rd = request.getRequestDispatcher("/empredg.jsp");
			rd.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
