package tech.csm.controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.csm.util.DBUtil;

public class MainController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		DBUtil.getSessionFactory();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String endPoint = req.getServletPath();

//		PrintWriter pw = resp.getWriter();

		if (endPoint.equals("/")) {
			RequestDispatcher rd = req.getRequestDispatcher("/villageRegdForm1.jsp");
			rd.forward(req, resp);
		}

		else if (endPoint.equals("/SignIn")) {
			
			String email = req.getParameter("mail");
			String password = req.getParameter("pwd");
			
			System.out.println("Email : "+email);
			System.out.println("Password : "+password);
			
//			RequestDispatcher rd = req.getRequestDispatcher("/getVillageRedgForm1");
//			rd.forward(req, resp);
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
