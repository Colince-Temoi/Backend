package tech.csm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import tech.csm.entity.Block;
import tech.csm.entity.Village;
import tech.csm.service.BlockService;
import tech.csm.service.BlockServiceImpl;
import tech.csm.service.VillageService;
import tech.csm.service.VillageServiceImpl;

public class GetRegdForm extends HttpServlet {
	
	private BlockService blockService;
	private VillageService villageService;
	
	public GetRegdForm() {
		blockService=new BlockServiceImpl();
		villageService=new VillageServiceImpl();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int pageSize=2;
		int pageNo=0;
		if(!(req.getParameter("pageNo")==null))
			pageNo=Integer.parseInt(req.getParameter("pageNo"));
			
		
		List<Block> blockList=blockService.getAllBlocks();
		
		
		req.setAttribute("blockList", blockList);
		Long noOfRecords=villageService.getTableSize();
		List<Integer> pageList=new ArrayList<>();		
		for(int i=0,j=1;i<noOfRecords;i+=pageSize,j++)
			pageList.add(j);
		
		
		
		System.out.println("pageSize"+pageSize+"  "+"pageNo="+pageNo);
		req.setAttribute("pageList", pageList);
		List<Village> villageList=villageService.getAllVillages(pageNo,pageSize);
		req.setAttribute("villageList", villageList);
		
		RequestDispatcher rd = req.getRequestDispatcher("villageRegdForm.jsp");
		rd.forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
