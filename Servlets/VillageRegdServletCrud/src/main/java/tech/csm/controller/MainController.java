package tech.csm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.csm.entity.County;
import tech.csm.service.CountyService;
import tech.csm.service.CountyServiceImpl;
import tech.csm.util.DBUtil;


@MultipartConfig(fileSizeThreshold = 1024*1024*5, maxFileSize = 1024*1024*10)
public class MainController extends HttpServlet {

	private CountyService countyservice;
	
	public MainController() {
		countyservice=new CountyServiceImpl();
	}
	
	private static final long serialVersionUID = 1L;
	@Override
	public void init() throws ServletException {
		DBUtil.getSessionFactory();
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String endPoint=req.getServletPath();
		
		PrintWriter pw = resp.getWriter();
		
		if(endPoint.equals("/")) {
			
			List<County> countyList=countyservice.getAllCounties();
			
			req.setAttribute("countyList", countyList);
			
			
			RequestDispatcher rd = req.getRequestDispatcher("/villageRegdForm.jsp");
			rd.forward(req, resp);
		}else if(endPoint.equals("/getConstituenciesByCountyId")) {
			 // Sample response
//			pw.println("<option>abc</option> <option>xyz</option> <option>def</option>"); 
			RequestDispatcher rd = req.getRequestDispatcher("/getConstituenciesByCountyId");
			rd.forward(req, resp);			
		}
	}
	@Override	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
