package tech.csm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.csm.entity.County;
import tech.csm.entity.Village;
import tech.csm.service.CountyService;
import tech.csm.service.CountyServiceImpl;
import tech.csm.service.VillageService;
import tech.csm.service.VillageServiceImpl;
import tech.csm.util.DBUtil;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 5, maxFileSize = 1024 * 1024 * 10)
public class MainController extends HttpServlet {
	
//	primitive dependencies
//	Setting the default page size.
	Integer pageSize = 4;  

	

//	Secondary dependencies
	private CountyService countyservice;
	private VillageService villageService;

	public MainController() {
		countyservice = new CountyServiceImpl();
		villageService=new VillageServiceImpl();
	}

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
			Integer pageNo = 0;  

//			Getting all Counties
			List<County> countyList = countyservice.getAllCounties();
			

			
			
//			Getting the table size: The count of records in Villages table
			Long noOfRecords=villageService.getTableSize();
			
//			A list to store the # of possible pages
			List<Integer> pageList=new ArrayList<>();	
			
			for(int i=0,j=1;i<noOfRecords;i+=pageSize,j++)
				pageList.add(j);
			
//			System.out.println(villageList);
//			System.out.println(noOfRecords);
//			System.out.println(pageList);
			
//			Grabbing the page number the user has clicked and storing it into the pageNo variable
			if(!(req.getParameter("pageNo")==null))
				pageNo=Integer.parseInt(req.getParameter("pageNo"));
			
			
//			Getting all Villages
//			List<Village> villageList=villageService.getAllVillages();
			List<Village> villageList=villageService.getAllVillages(pageNo,pageSize);
			
//			System.out.println(page	

			req.setAttribute("countyList", countyList);
			req.setAttribute("villageList", villageList);
			req.setAttribute("pageList", pageList);

			RequestDispatcher rd = req.getRequestDispatcher("/villageRegdForm.jsp");
			rd.forward(req, resp);
		} else if (endPoint.equals("/getConstituenciesByCountyId")) {
			// Sample response
//			pw.println("<option>abc</option> <option>xyz</option> <option>def</option>"); 
			RequestDispatcher rd = req.getRequestDispatcher("/getConstituenciesByCountyId");
			rd.forward(req, resp);
		} else if (endPoint.equals("/saveVlg")) {
			RequestDispatcher rd = req.getRequestDispatcher("/saveVillage");
			rd.forward(req, resp);
		}else if(endPoint.equals("/downloadFile")) {
			RequestDispatcher rd = req.getRequestDispatcher("/download");
			rd.forward(req, resp);	
		}else if(endPoint.equals("/countyFilter")) {
			RequestDispatcher rd = req.getRequestDispatcher("/filter");
			rd.forward(req, resp);	
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
