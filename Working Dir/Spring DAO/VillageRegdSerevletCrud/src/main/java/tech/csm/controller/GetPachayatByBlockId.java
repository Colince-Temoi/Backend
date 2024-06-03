package tech.csm.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import tech.csm.entity.Panchayat;
import tech.csm.service.PanchayatService;
import tech.csm.service.PanchayatServiceImpl;

public class GetPachayatByBlockId extends HttpServlet {
	private PanchayatService panchayatService;
	public GetPachayatByBlockId() {
		panchayatService=new PanchayatServiceImpl();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		Integer blockId=Integer.parseInt(req.getParameter("blockId"));
		List<Panchayat> panchayatList= panchayatService.getPanchayatByBlockId(blockId);
		String res="<option value='0'>-select-</option>";
		for(Panchayat p:panchayatList) {
			res+="<option value='"+p.getPanchayatId()+"'>"+p.getName()+"</option>";
		}
		resp.getWriter().println(res);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
