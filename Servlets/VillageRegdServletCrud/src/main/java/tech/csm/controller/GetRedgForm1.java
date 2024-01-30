package tech.csm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.csm.entity.County;
import tech.csm.entity.Village;
import tech.csm.service.CountyService;
import tech.csm.service.CountyServiceImpl;
import tech.csm.service.VillageService;
import tech.csm.service.VillageServiceImpl;

public class GetRedgForm1 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
//	Secondary dependencies
	private CountyService countyservice;
	private VillageService villageService;

	public GetRedgForm1() {
		countyservice = new CountyServiceImpl();
		villageService=new VillageServiceImpl();
	}

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");		
		PrintWriter pw = resp.getWriter();
		
//		pw.println("In get redg form 1");
		
//		Getting all Counties
		List<County> countyList = countyservice.getAllCounties();
		
//		Getting all Villages
		List<Village> villageList=villageService.getAllVillages();
		
		req.setAttribute("countyList", countyList);
		req.setAttribute("villageList", villageList);

		RequestDispatcher rd = req.getRequestDispatcher("/villageRegdForm1.jsp");
		rd.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
