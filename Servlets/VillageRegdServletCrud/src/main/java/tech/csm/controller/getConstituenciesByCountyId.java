package tech.csm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.csm.entity.Constituency;
import tech.csm.service.ConstituencyService;
import tech.csm.service.ConstituencyServiceImpl;

public class getConstituenciesByCountyId extends HttpServlet {

	ConstituencyService constituencyService;
	
	public getConstituenciesByCountyId() {
		constituencyService = new ConstituencyServiceImpl();
	}
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Integer countyId = Integer.parseInt(req.getParameter("countyId"));
		
		PrintWriter pw = resp.getWriter();
		// Sample response
//		pw.println("<option>abc</option> <option>xyz</option> <option>def</option>");  
//		pw.println(countyId);
		
		List<Constituency> constituencies = constituencyService.getConstituencieByCountyId(countyId);
		String res="<option value='0'>-select-</option>";
		for(Constituency c:constituencies) {
			res+="<option value='"+c.getConstituencyId()+"'>"+c.getName()+"</option>";
		}
		pw.println(res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
