package tech.csm.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tech.csm.util.DBUtil;


@MultipartConfig(fileSizeThreshold = 1024*1024*5, maxFileSize = 1024*1024*10)
public class MainController extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		DBUtil.getSessionFactory();
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String endPoint=req.getServletPath();
		if(endPoint.equals("/login")) {
			RequestDispatcher rd = req.getRequestDispatcher("/login");
			rd.forward(req, resp);
		}
		else if(endPoint.equals("/userregd")) {
			RequestDispatcher rd = req.getRequestDispatcher("/userregd");
			rd.forward(req, resp);
		}
	}
	@Override	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
