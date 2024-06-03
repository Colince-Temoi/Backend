package tech.csm.controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
		if(endPoint.equals("/getRegdForm")) {
			RequestDispatcher rd = req.getRequestDispatcher("/getRegdForm");
			rd.forward(req, resp);
		}else if(endPoint.equals("/getPachayatByBlockId")) {
			RequestDispatcher rd = req.getRequestDispatcher("/getPachayatByBlockId");
			rd.forward(req, resp);			
		}else if(endPoint.equals("/saveVlg")) {
			RequestDispatcher rd = req.getRequestDispatcher("/saveVlg");
			rd.forward(req, resp);		
		}else if(endPoint.equals("/downloadFile")) {
			RequestDispatcher rd = req.getRequestDispatcher("/downloadFile");
			rd.forward(req, resp);	
		}
		else if(endPoint.equals("/blockFilter")) {
			RequestDispatcher rd = req.getRequestDispatcher("/blockFilter");
			rd.forward(req, resp);	
		}
		else if(endPoint.equals("/getRegdForm2")) {
			RequestDispatcher rd = req.getRequestDispatcher("/getRegdForm2");
			rd.forward(req, resp);	
		}
	}
	@Override	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
