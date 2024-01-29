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

public class FilterController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private CountyService countyservice;
	private VillageService villageService;

	public FilterController() {
		countyservice = new CountyServiceImpl();
		villageService=new VillageServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		PrintWriter pw = resp.getWriter();		
//		System.out.println("Hello request is coming");
//		Get County Id based on which to perform filtration
		Integer cId=Integer.parseInt(req.getParameter("countyfilter"));
		
//		Perform appropriate modifications here to make sure that pagination also applies for the filtered villages by county id
		
		List<Village> villageList=villageService.getVillageByBlockId(cId);
		
//		Set the list of filtered villages to request object 
		req.setAttribute("villageList", villageList);
		
//		Re-use this: we need it in the form as well
		List<County> countyList = countyservice.getAllCounties();
		req.setAttribute("countyList", countyList);
		
//		Dispatch the things back to the Form
		RequestDispatcher rd = req.getRequestDispatcher("/villageRegdForm.jsp");
		rd.forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
